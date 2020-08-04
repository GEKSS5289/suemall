package com.sue.exception.usercenterexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/2 19:05
 */

public class UserCenterException extends AbstractException {
    public UserCenterException(Integer code) {
        this.setCode(code);
    }
}
