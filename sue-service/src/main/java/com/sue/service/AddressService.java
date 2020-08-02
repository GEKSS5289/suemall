package com.sue.service;

import com.sue.pojo.UserAddress;
import com.sue.pojo.dto.AddressDTO;

import java.util.List;

/**
 * @author sue
 * @date 2020/8/2 15:08
 */

public interface AddressService {
    /***
     * 根据用户id查询用户地址列表
     * @param userId
     * @return
     */
    public List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     * @param addressDTO
     */
    public void addNewUserAddress(AddressDTO addressDTO);


    /**
     * 用户修改地址
     * @param addressDTO
     */
    public void updateUserAddress(AddressDTO addressDTO);


    /**
     * 用户删除地址
     * @param userId
     * @param addressId
     */
    public void deleteUserAddress(String userId,String addressId);

}
