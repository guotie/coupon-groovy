

import com.mall.coupon.groovy.model.Coupon
import com.mall.coupon.groovy.service.UserCouponService;

class Dog {

    def say() {
        UserCouponService.addUserCoupon("SAVE8", new Coupon());
        println('hello groovy')
    }
}