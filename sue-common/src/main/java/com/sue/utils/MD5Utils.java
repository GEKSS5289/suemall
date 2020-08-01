package com.sue.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String getMD5Str(String strValue) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newstr;
    }
}
