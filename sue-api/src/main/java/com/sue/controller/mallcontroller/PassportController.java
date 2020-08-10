package com.sue.controller.mallcontroller;

import com.sue.core.util.ClearDataUtils;
import com.sue.exception.mallexception.PassportException;
import com.sue.pojo.Users;
import com.sue.pojo.dto.malldto.ShopcartDTO;
import com.sue.pojo.dto.malldto.UserDTO;
import com.sue.pojo.dto.malldto.UserRegisterDTO;
import com.sue.pojo.vo.UsersVO;
import com.sue.service.mallservice.UserService;
import com.sue.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sue
 * @date 2020/7/31 10:29
 */

@Api(value = "注册登录", tags = {"用于相关接口"})
@RestController
@RequestMapping("passport")
@Validated
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;


    @Autowired
    private RedisOperator redisOperator;
    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {


        if (StringUtils.isBlank(username)) {
            throw new PassportException(10001);
        }

        //查找注册的用户名是否存在
        if (userService.queryUsernameIsExist(username)) {
            throw new PassportException(10002);
        }

        return IMOOCJSONResult.ok();

    }


    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult createUser(
            @RequestBody UserRegisterDTO userRegisterDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {


        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(userRegisterDTO, userDTO);

        Users user = userService.createUser(userDTO);

//        ClearDataUtils.setNullProperties(user);

        //实现用户的redis会话
        UsersVO usersVO = conventUsersVO(user);

        CookieUtils.setCookie(
                request,
                response,
                "user",
                JsonUtils.objectToJson(usersVO),
                true
        );

        synchShopcartData(user.getId(),request,response);

        return IMOOCJSONResult.ok();

    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(
            @Valid @RequestBody UserDTO userDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {


        Users users = userService.queryUserForLogin(
                userDTO.getUsername(),
                MD5Utils.getMD5Str(userDTO.getPassword())
        );

        if (users == null) {
           throw new PassportException(10000);
        }

//        ClearDataUtils.setNullProperties(users);

        UsersVO usersVO = conventUsersVO(users);

        CookieUtils.setCookie(
                request,
                response,
                "user",
                JsonUtils.objectToJson(usersVO),
                true
        );

        synchShopcartData(users.getId(),request,response);

        return IMOOCJSONResult.ok(users);

    }


    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        //清除用户相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");
        redisOperator.del(REDIS_USER_TOKEN+":"+userId);
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);
        return IMOOCJSONResult.ok();
    }


    /**
     * 注册成功后同步cookie和reids中的购物车数据
     */
    private void synchShopcartData(String userId,HttpServletRequest request,HttpServletResponse response){
        /**
         * 1.redis中无数据，如果cookie中的购物车为空，则不做任何处理
         * 2.如果cookie中的购物车不为空，此时直接放入redis
         * 3.redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
         * 4.如果cookie中的购物车不为空，如果cookie中的某个商品在redis中存在，则以cookie为主，删除redis中的数据，cookie覆盖redis
         * 5.同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步的
         *
         */
        //从redis中获取购物车
        String shopRedis = redisOperator.get(FOODIE_SHOPCART+":"+userId);
        //从cookie中获取购物车
        String shocartStrCookie = CookieUtils.getCookieValue(request,FOODIE_SHOPCART,true);
        if(StringUtils.isBlank(shopRedis)){
            if(StringUtils.isNotBlank(shocartStrCookie)){
                redisOperator.set(FOODIE_SHOPCART+":"+userId,shocartStrCookie);
            }
        }else{
            if(StringUtils.isNotBlank(shocartStrCookie)){
                /**
                 * 1.已经存在的，吧cookie中应对的数量，覆盖redis（参考京东）
                 * 2.该商品标记为待删除，统一放入一个待删除的list
                 * 3.从cookie中清理所有待删除的list
                 * 4.合并redis和cookie中的数据
                 * 5.更新到redis和cookie中
                 */
                List<ShopcartDTO> shopCartDTOList = JsonUtils.jsonToList(shopRedis,ShopcartDTO.class);
                List<ShopcartDTO> shopCookie = JsonUtils.jsonToList(shocartStrCookie,ShopcartDTO.class);
                List<ShopcartDTO> pengdingDeleteList = new ArrayList<>();
                for(ShopcartDTO redisShopCart:shopCartDTOList){
                    String redisSpecId = redisShopCart.getSpecId();
                    for(ShopcartDTO cookieShopcart:shopCookie){
                        String cookieSpecId = cookieShopcart.getSpecId();
                        if(redisSpecId.equals(cookieSpecId)){
                            redisShopCart.setBuyCounts(cookieShopcart.getBuyCounts());
                            pengdingDeleteList.add(cookieShopcart);
                        }
                    }
                }
                shopCookie.removeAll(pengdingDeleteList);
                shopCartDTOList.addAll(shopCookie);
                //更新到redis和cookie
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,JsonUtils.objectToJson(shopCartDTOList),true);
                redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(shopCartDTOList));
            }else{
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,shopRedis,true);
            }
        }
    }


    private UsersVO conventUsersVO(Users user){
        //实现用户的redis会话
        String uniqueToken = UUID.randomUUID().toString().trim();

        redisOperator.set(REDIS_USER_TOKEN+":"+user.getId(),uniqueToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        return usersVO;
    }
}
