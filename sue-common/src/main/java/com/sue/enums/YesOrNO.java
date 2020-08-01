package com.sue.enums;

/**
 * @author sue
 * @date 2020/8/1 14:13
 */

import lombok.Getter;

/**
 * 是否枚举
 */
@Getter
public enum YesOrNO {
    NO(0,"否"),
    YES(1,"是");
    public final Integer type;
    public final String value;

    YesOrNO(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
