package com.sue.exception.usercenterexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/4 12:35
 */

public class CenterException extends AbstractException {
    public CenterException(Integer code) {
        this.setCode(code);
    }
}
