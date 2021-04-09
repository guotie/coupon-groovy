package com.mall.coupon.core.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class  Coupon {
    private Integer id;
    private String couponBatchCode;
    // (1 固定折扣、2 固定减免金额、3 固定金额) 4: 满减； 5: 折扣， 6: 满赠； 7: 返还； 8: 支付方式； 9: 兑换；
    private String couponType;
    private String useType;
    private String usetimeType;
    private Date startTime;
    private Date endTime;
    private BigDecimal expiryTime;
    private Integer limitPerUser;
    private Integer limitNumber;
    private Integer prodNumber;
    private Integer extendNumber;
    private String productId;
    private String productAmount;
    private BigDecimal discountNum;
    private String calculateExpres;
    private String couponBatchDesc;
    private Date createTime;
    private String isDelete;
    private String activityType;

    // extend field
    private String version;
    private String couponName;
    private String claimType;
    private String channelType;
    private String groupType;
    private String subCouponType;
    private String discountPkgType;
    private String discountPkgMonth;
    private String logoActive;
    private String logoInactive;
    private String couponRules;
    private String feTag;
    private Integer nominal;
    private Integer minBuyAmount;
    private Integer minBuyCount;
    private Integer maxDiscountCount;
    private String isThird;
    private Integer claimCount;
    private String status;
    private String mallId;
    private Integer price;
    private Integer maxDiscountAmount;
    private String currency;

    // 是否仅通用券
    // 使用类型 (1 购卡、2 购套餐、3 通用) 4：指定分类； 5: 指定商品spu; 6: 指定sku
    public boolean isGeneralCoupon() {
        // 为空或为零时 不限用户
        return useType == null || "".equals(useType) || "3".equals(useType);
    }

    // 是否有最低使用门槛
    public boolean hasMinThreshold() {
        // 满减且按数量满减 满二减一
        if (isMoneyoffByQuantityCoupon()) {
            return minBuyCount != null && minBuyCount != 0;
        }
        return minBuyAmount != null && minBuyAmount != 0;
    }

    public boolean reachMinThreshold(Integer amt) {
        if (!hasMinThreshold()) {
            return true;
        }
        if (isMoneyoffByQuantityCoupon()) {
            return amt >= minBuyCount;
        }
        return amt >= minBuyAmount;
    }

    // 折扣
    public boolean isDiscountCoupon() {
        return "1".equals(couponType) || "5".equals(couponType);
    }

    // 满减
    public boolean isMoneyoffCoupon() {
        return "2".equals(couponType);
    }

    // 满减，且按数量满减
    public boolean isMoneyoffByQuantityCoupon() {
        return isMoneyoffCoupon() && "4".equals(subCouponType);
    }

    // 满赠
    public boolean isGiftCoupon() {
        return "2".equals(couponType);
    }

    public boolean notLimitUser() {
        // 为空或为零时 不限用户
        return groupType == null || "".equals(groupType) || "0".equals(groupType);
    }

    public boolean notBindingType() {
        return claimType == null || "".equals(claimType) || "0".equals(claimType);
    }
}
