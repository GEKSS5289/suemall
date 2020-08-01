package com.sue.enums;

import lombok.Getter;

/**
 * @author sue
 * @date 2020/8/1 9:21
 */
@Getter
public enum  Sex {
    WOMAN(0,"女"),
    MAN(1,"男"),
    secret(2,"保密");
    public Integer type;
    public String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
