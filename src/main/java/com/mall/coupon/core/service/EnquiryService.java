package com.mall.coupon.core.service;

// 调用规则脚本询价

import com.mall.coupon.core.model.EnquiryResult;
import com.mall.coupon.core.model.Coupon;
import com.mall.coupon.core.model.Order;
import com.mall.coupon.core.model.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EnquiryService {

    // 询价
    public static EnquiryResult enquiry(Order order, String code) {
        EnquiryResult result = new EnquiryResult();
        Coupon coupon = CouponBatchService.getCouponByCode(code);

        if (coupon == null) {
            log.warn("not found coupon {}", code);
            return result;
        }

        return result;
    }

    private static Integer calcDiscountAmount(Order order, Coupon coupon, OrderItem hitSku) {
        if (coupon.isGeneralCoupon()) {
            return order.getTotalAmount();
        }
        // 仅适用于特定的商品, 轮询获取最大的
        Integer maxAmount = 0;
        for (OrderItem item: order.getItems()) {
            Integer amount = item.getTotalAmount(); // price * 数量
            // todo 判断 coupon 是否可以用于该 sku
            if (true) {
                if (amount > maxAmount) {
                    maxAmount = amount;
                    BeanUtils.copyProperties(item, hitSku);
                }
            }
        }
        return maxAmount;
    }

    public static void enquiry(Order order, Coupon coupon, EnquiryResult result) {
        OrderItem hitSku = new OrderItem();
        Integer amount = calcDiscountAmount(order, coupon, hitSku);
        if (!coupon.reachMinThreshold(amount)) {
            // 未达到最低
            result.setResultCode("NotReachThreshold");
            result.setResultMsg("未达到电子券最低消费门槛");
            return;
        }

        Integer discount = 0;
        switch (coupon.getCouponType()) {
            case "1":
            case "5":
                // 固定折扣
                discount = amount * coupon.getNominal() / 100;
                result.setCouponDiscount(discount);
                result.setResultCode("OK");
                break;

            case "2":
            case "4":
                // 满减
                if (coupon.isMoneyoffByQuantityCoupon()) {
                    // 获取打折的 order item
                    discount = hitSku.getTotalAmount() == null ? 0 : hitSku.getTotalAmount();
                } else {
                    discount = amount > coupon.getNominal() ? coupon.getNominal() : amount;
                }
                result.setCouponDiscount(discount);
                result.setResultCode("OK");
                break;

            case "3":
                // 固定金额, 可视为满减的特殊情况
                result.setCouponDiscount(coupon.getNominal());
                result.setResultCode("OK");
                break;

            case "6":
                // 满赠
            case "7":
                // 返还
            case "9":
                // 兑换
                result.setResultCode("NotImplement");
                return;

            case "8":
                // 支付方式
                // todo 判断支付方式是否满足打折要求
                result.setCouponDiscount(discount);
                result.setResultCode("OK");
                break;

            default:
                log.warn("invalid coupon type: {} {}", coupon.getId(), coupon.getCouponType());
                return;
        }
        // 订单的每个商品的分摊优惠金额
//        } else {
//
//        }

    }

    // 1. 一个 order 可以使用多张优惠券的计算比较复杂，这里就不
    // 2. 一个电子券可以用于order中的多个sku时，需要计算如何应用才能达到最优组合
    // 3. 考虑互斥的情况
    // 4. 考虑券的叠加顺序
    public static void enquiry(Order order, List<Coupon> couponList, EnquiryResult result) {


    }

    public static List<Coupon> cloneCouponList(List<Coupon> src) {
        List<Coupon> dst = new ArrayList<>();
        for (Coupon item: src) {
            Coupon dup = new Coupon();
            BeanUtils.copyProperties(item, dup);

            dst.add(dup);
        }
        return dst;
    }
}
