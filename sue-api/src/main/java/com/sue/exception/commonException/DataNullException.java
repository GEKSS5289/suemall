package com.sue.exception.commonException;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/4 12:51
 */

public class DataNullException extends AbstractException {
    public DataNullException(Integer code) {
        this.setCode(code);
    }
}
