package com.sue.controller.mallcontroller;

import com.sue.pojo.dto.malldto.ShopcartDTO;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import com.sue.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/2 11:10
 */

@Api(value = "购物车接口controller", tags = {"购物车接口相关api"})
@RestController
@RequestMapping("/shopcart")
public class ShopCatController extends BaseController {


    @Autowired
    private RedisOperator redisOperator;
    public static final Logger log = LoggerFactory.getLogger(ShopCatController.class);

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestParam String userId,
            @RequestBody ShopcartDTO shopcartDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        //TODO 前端用户在登录情况下 添加商品到购物车 会同时在后端同步购物车到redis缓存
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART+":"+userId);
        List<ShopcartDTO> shopcartDTOS = null;

        if(StringUtils.isNotBlank(shopcartJson)){
            //redis中已经有购物车了
            shopcartDTOS = JsonUtils.jsonToList(shopcartJson,ShopcartDTO.class);
            //判断购物车中是否存在已有商品，如果有的话counts累加
            boolean isHaving = false;
            for(ShopcartDTO sc : shopcartDTOS){
                String tmpSpecId = sc.getSpecId();
                if(tmpSpecId.equals(shopcartDTO.getSpecId())){
                    sc.setBuyCounts(sc.getBuyCounts()+shopcartDTO.getBuyCounts());
                    isHaving = true;
                }
            }
            if(!isHaving){
                shopcartDTOS.add(shopcartDTO);
            }
        }else{
            //redis中没有购物车
            shopcartDTOS = new ArrayList<>();
            shopcartDTOS.add(shopcartDTO);
        }

        redisOperator.set(FOODIE_SHOPCART+":"+userId, JsonUtils.objectToJson(shopcartDTOS));

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "删除商品", notes = "删除商品", httpMethod = "POST")
    @PostMapping("del")
    public IMOOCJSONResult del(
            @RequestParam String userId,
            @RequestBody String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }


        //TODO 用户在页面删除购物车中的商品数据，如果此时用户以登录 则需要同步删除redis购物车中的数据
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART+":"+userId);
        if(StringUtils.isNotBlank(shopcartJson)){
            //redis 中已经有购物车了
            List<ShopcartDTO> shopcartDTOS = JsonUtils.jsonToList(shopcartJson,ShopcartDTO.class);
            //判断购物车中是否存在已有商品，如果有的话则删除
            for(ShopcartDTO sc:shopcartDTOS){
                String tempSpecId = sc.getSpecId();
                if(tempSpecId.equals(itemSpecId)){
                    shopcartDTOS.remove(sc);
                    break;
                }
            }
            //覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART+":"+userId,JsonUtils.objectToJson(shopcartDTOS));
        }

        return IMOOCJSONResult.ok();
    }
}
