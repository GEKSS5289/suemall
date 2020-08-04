package com.sue.exception.mallexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/4 13:01
 */

public class OrdersException extends AbstractException {
    public OrdersException(Integer code) {
        this.setCode(code);
    }
}
