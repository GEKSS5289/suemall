package com.sue.controller.mallcontroller;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * @author sue
 * @date 2020/8/2 9:17
 */

@RestController
public class BaseController {
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String IMAGES_ERVER_URL = "http://localhost:8088/foodie/faces";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    //微信支付成功 -> 支付中心 -> 天天吃货平台 （会调通知的Url）
    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";


    //用户上传头像地址
    public static final String IMAGE_USER_FACE_LOCATION =
            "E:"+ File.separator+"SbootProject"+File.separator+"images"+File.separator+"foodie"+File.separator+"faces";
}
