package com.sue.exception;

import lombok.Data;

/**
 * @author sue
 * @date 2020/8/2 19:06
 */

@Data
public abstract class AbstractException extends RuntimeException {
    private Integer code;
}
