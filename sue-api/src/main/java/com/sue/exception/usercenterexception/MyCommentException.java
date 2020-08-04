package com.sue.exception.usercenterexception;

import com.sue.exception.AbstractException;

/**
 * @author sue
 * @date 2020/8/4 12:35
 */

public class MyCommentException  extends AbstractException {
    public MyCommentException(Integer code) {
        this.setCode(code);
    }
}