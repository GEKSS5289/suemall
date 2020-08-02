package com.sue.exception.passportexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/2 19:05
 */

public class PassportException extends AbstractException {
    public PassportException(Integer code) {
        this.setCode(code);
    }
}
