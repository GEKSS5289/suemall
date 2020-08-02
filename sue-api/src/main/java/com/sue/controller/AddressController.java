package com.sue.controller;

import com.sue.pojo.dto.AddressDTO;
import com.sue.service.AddressService;
import com.sue.utils.IMOOCJSONResult;
import com.sue.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sue
 * @date 2020/8/2 15:02
 */
@Api(value = "用户地址相关",tags = {"地址相关API接口"})
@RequestMapping("address")
@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;


    @ApiOperation(value = "根据用户id查询用户地址列表",notes = "根据用户id查询用户地址列表",httpMethod = "GET")
    @GetMapping("/list")
    public IMOOCJSONResult list(@RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("");
        }

      return IMOOCJSONResult.ok(addressService.queryAll(userId));
    }


    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod = "POST")
    @PostMapping("/add")
    public IMOOCJSONResult add(
            @RequestBody AddressDTO addressDTO
    ){
        IMOOCJSONResult imoocjsonResult = checkAddress(addressDTO);
        if(imoocjsonResult.getStatus() != 200){
            return imoocjsonResult;
        }
        addressService.addNewUserAddress(addressDTO);
        return IMOOCJSONResult.ok();
    }




    @ApiOperation(value = "用户地址修改",notes = "用户地址修改",httpMethod = "POST")
    @PostMapping("/update")
    public IMOOCJSONResult update(
            @RequestBody AddressDTO addressDTO
    ){

        if(StringUtils.isBlank(addressDTO.getAddressId())){
            return IMOOCJSONResult.errorMsg("修改地址出错");
        }

        IMOOCJSONResult imoocjsonResult = checkAddress(addressDTO);
        if(imoocjsonResult.getStatus() != 200){
            return imoocjsonResult;
        }

        addressService.updateUserAddress(addressDTO);
        return IMOOCJSONResult.ok();
    }




    private IMOOCJSONResult checkAddress(AddressDTO addressDTO) {
        String receiver = addressDTO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressDTO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressDTO.getProvince();
        String city = addressDTO.getCity();
        String district = addressDTO.getDistrict();
        String detail = addressDTO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }

}
