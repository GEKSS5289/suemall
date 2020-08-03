package com.sue.service.mallservice.impl;

import com.sue.enums.YesOrNO;
import com.sue.mapper.UserAddressMapper;
import com.sue.pojo.UserAddress;
import com.sue.pojo.dto.AddressDTO;
import com.sue.service.mallservice.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author sue
 * @date 2020/8/2 15:08
 */

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;


    /***
     * 根据用户id查询用户地址列表
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }


    /**
     * 用户新增地址
     *
     * @param addressDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressDTO addressDTO) {

        Integer isDefaultAdderss = 0;
        String addressId = sid.nextShort();
        List<UserAddress> userAddresses = this.queryAll(addressDTO.getUserId());
        if(userAddresses == null || userAddresses.isEmpty()||userAddresses.size()==0){
            isDefaultAdderss = 1;
        }

        UserAddress newUserAddress = new UserAddress();
        BeanUtils.copyProperties(addressDTO,newUserAddress);
        newUserAddress.setId(addressId);
        newUserAddress.setIsDefault(isDefaultAdderss);
        newUserAddress.setCreatedTime(new Date());
        newUserAddress.setUpdatedTime(new Date());


        userAddressMapper.insert(newUserAddress);

    }


    /**
     * 用户修改地址
     *
     * @param addressDTO
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressDTO addressDTO) {
        String addressId = addressDTO.getAddressId();
        UserAddress updateAddress = new UserAddress();
        BeanUtils.copyProperties(addressDTO,updateAddress);
        updateAddress.setId(addressId);
        updateAddress.setUpdatedTime(new Date());
        //空属性不覆盖
        userAddressMapper.updateByPrimaryKeySelective(updateAddress);
    }


    /**
     * 用户删除地址
     *
     * @param userId
     * @param addressId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("id",addressId);
        userAddressMapper.deleteByExample(example);
    }


    /**
     * 用户设置默认地址
     *
     * @param userId
     * @param addressId
     */

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {


        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("isDefault", YesOrNO.YES.type);
        List<UserAddress> userAddresses = userAddressMapper.selectByExample(example);
        userAddresses.forEach(i->{
            i.setIsDefault(YesOrNO.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(i);
        });

        UserAddress defualtUserAddress = new UserAddress();
        defualtUserAddress.setIsDefault(YesOrNO.YES.type);
        defualtUserAddress.setUserId(userId);
        defualtUserAddress.setId(addressId);

        userAddressMapper.updateByPrimaryKeySelective(defualtUserAddress);



    }


    /**
     * 根据用户Id和地址Id，查询具体用户地址对象信息
     *
     * @param userId
     * @param addressId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {

        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("id",addressId);

        return userAddressMapper.selectOneByExample(example);
    }
}
