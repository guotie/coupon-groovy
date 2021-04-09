package com.mall.coupon.core.model;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String name;
    private String productSn;
    private Integer catId;
    private Integer brandId;
    private String brandName;
    private Integer price;
    private Integer promotionPrice;
}
