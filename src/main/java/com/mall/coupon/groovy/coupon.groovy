package com.mall.coupon.groovy

import com.mall.coupon.core.model.Coupon
import com.mall.coupon.core.model.Order
import com.mall.coupon.core.model.EnquiryResult
//import com.mall.coupon.core.model.OrderItem

//import com.mall.coupon.groovy.DiscountCoupon

// 询价入口
static def enquiry(Order order, Coupon coupon, EnquiryResult result) {
    // 对order中的每个item计算优惠
    switch (coupon.getCouponType()) {
        case "1":
        case "5":
            return DiscountCoupon.enquiry(order, coupon, result)
        case "2":
        case "4":
            return Moneyoff.enquiry(order, coupon, result)
    }
    // 对整个订单计算优惠金额
}
