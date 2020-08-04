package com.sue.core.util;

import com.sue.pojo.Users;

/**
 * @author sue
 * @date 2020/8/4 12:12
 */

public class ClearDataUtils {
    public static void setNullProperties(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
    }
}
