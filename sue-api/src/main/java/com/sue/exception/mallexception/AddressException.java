package com.sue.exception.mallexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/4 13:16
 */

public class AddressException  extends AbstractException {
    public AddressException(Integer code) {
        this.setCode(code);
    }
}
