package com.sue.core.util;

import com.sue.controller.mallcontroller.BaseController;
import com.sue.pojo.Orders;
import com.sue.pojo.vo.MerchantOrdersVO;
import com.sue.pojo.vo.OrderVO;
import com.sue.utils.IMOOCJSONResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author sue
 * @date 2020/8/4 13:02
 */

public class PayCenterDataUtils {

    public static boolean sendPayCenter(OrderVO order, RestTemplate restTemplate){
        //向支付中心发送当前订单，用于保存支付中心订单数据
        MerchantOrdersVO merchantOrdersVO = order.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(BaseController.payReturnUrl);
        merchantOrdersVO.setAmount(1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "4588687-496966859");
        headers.add("password", "po13-e0o3-r01p-f0o3");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO,headers);

        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity(BaseController.payReturnUrl, entity, IMOOCJSONResult.class);

        IMOOCJSONResult paymentResult = responseEntity.getBody();

        if(paymentResult.getStatus() != 200){
            return false;
        }else{
            return true;
        }
    }
}
