package com.sue.interceptor;

import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.JsonUtils;
import com.sue.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sue
 * @date 2020/8/10 9:58
 */

public class UserTokenInterceptor implements HandlerInterceptor {

    private static final String REDIS_USER_TOKEN = "redis_user_token";

    @Autowired
    private RedisOperator redisOperator;


    /**
     * 拦截请求，在访问controller调用之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userToken = request.getHeader("headerUserToken");
        String userId = request.getHeader("headerUserId");

        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
                String uniqueToken = redisOperator.get(REDIS_USER_TOKEN+":"+userId);
                if(StringUtils.isNotBlank(uniqueToken)){
                    if(uniqueToken.equals(userToken)){
                        return true;
                    }else{
                        this.returnErrorResponse(response, IMOOCJSONResult.errorMsg("账号在异地登录"));
                        return false;
                    }
                }else{
                    this.returnErrorResponse(response, IMOOCJSONResult.errorMsg("请登录..."));
                    return false;
                }
        }else{
            this.returnErrorResponse(response, IMOOCJSONResult.errorMsg("请登录..."));
            return false;
        }
//        /**
//         * false: 请求被拦截，被驳回，验证出问题
//         * true：请求经过校验后 成功放行
//         */
//        return true;
    }

    /**
     * 请求访问controller之后渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    /**
     * 请求访问controller之后，渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse response, IMOOCJSONResult result) {
        ServletOutputStream out = null;

        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException var13) {
            var13.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException var12) {
                var12.printStackTrace();
            }

        }

    }
}
