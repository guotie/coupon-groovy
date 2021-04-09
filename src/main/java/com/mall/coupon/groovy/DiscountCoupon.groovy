package com.mall.coupon.groovy

import com.mall.coupon.core.model.Coupon
import com.mall.coupon.core.model.EnquiryResult
import com.mall.coupon.core.model.Order

class DiscountCoupon {
    static void enquiry(Order order, Coupon coupon, EnquiryResult result) {
        println("discount coupon enquiry")
    }
}
