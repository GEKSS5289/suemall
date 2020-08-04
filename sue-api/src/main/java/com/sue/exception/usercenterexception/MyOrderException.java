package com.sue.exception.usercenterexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/4 12:35
 */

public class MyOrderException extends AbstractException {
    public MyOrderException(Integer code) {
        this.setCode(code);
    }
}
