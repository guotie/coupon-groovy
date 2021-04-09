package com.mall.coupon.groovy

import com.mall.coupon.core.model.Coupon
import com.mall.coupon.core.service.UserCouponService;

class Dog {
    def say(String[] args) {
        Coupon coupon = new Coupon()
        coupon.setCouponBatchCode("SAVE8")
        UserCouponService.addUserCoupon("SAVE8", coupon);
        println('hello groovy: ' + args)
    }

    def main(String[] args) {
        say(args)
    }
}