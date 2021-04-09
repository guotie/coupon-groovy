package com.mall.coupon.core.service;

public class User {
    public static String userIdentify(String userType, String userId) {
        return userType + "-" + userId;
    }
}
