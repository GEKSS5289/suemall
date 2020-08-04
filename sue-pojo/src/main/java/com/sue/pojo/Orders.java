package com.sue.pojo;

import com.sue.enums.YesOrNO;
import com.sue.pojo.dto.malldto.SubmitOrderDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@Data
@NoArgsConstructor
public class Orders {
    @Id
    private String id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "receiver_name")
    private String receiverName;
    @Column(name = "receiver_mobile")
    private String receiverMobile;
    @Column(name = "receiver_address")
    private String receiverAddress;
    @Column(name = "total_amount")
    private Integer totalAmount;
    @Column(name = "real_pay_amount")
    private Integer realPayAmount;
    @Column(name = "post_amount")
    private Integer postAmount;
    @Column(name = "pay_method")
    private Integer payMethod;
    @Column(name = "left_msg")
    private String leftMsg;
    private String extand;
    @Column(name = "is_comment")
    private Integer isComment;
    @Column(name = "is_delete")
    private Integer isDelete;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;


    public Orders(String orderId, SubmitOrderDTO submitOrderDTO,UserAddress userAddress){
        this.setId(orderId);
        this.setUserId(submitOrderDTO.getUserId());
        this.setReceiverName(userAddress.getReceiver());
        this.setReceiverMobile(userAddress.getMobile());
        this.setReceiverAddress(userAddress.getReceiver()+" "+userAddress.getCity()+" "+userAddress.getDistrict()+" "+userAddress.getDistrict());
        this.setPostAmount(0);
        this.setPayMethod(submitOrderDTO.getPayMethod());
        this.setLeftMsg(submitOrderDTO.getLeftMsg());
        this.setIsComment(YesOrNO.NO.type);
        this.setIsDelete(YesOrNO.NO.type);
        this.setCreatedTime(new Date());
        this.setUpdatedTime(new Date());
    }

}