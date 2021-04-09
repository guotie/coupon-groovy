package com.mall.coupon.core;

import com.mall.coupon.core.model.*;
import com.mall.coupon.core.service.CouponBatchService;
import com.mall.coupon.core.service.OrderService;
import com.mall.coupon.core.service.UserCouponService;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import lombok.extern.slf4j.Slf4j;

//import org.codehaus.groovy.jsr223.GroovyScriptEngineFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;

//import javax.naming.Binding;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class CouponGroovyApplication {
    public static void main(String[] args) throws Exception {
//        runScriptByEngine("src/main/java/com/mall/coupon/groovy/", "Dog.groovy");
//        runScriptByEngine("src/main/java/com/mall/coupon/groovy/", "Dog.groovy");
        runByReadFile("D:\\xw\\learn\\coupon-groovy\\src\\main\\java\\com\\mall\\coupon\\groovy\\Dog.groovy",
                "main", new String[]{"hahaha", "vavava"});

        testEnquiry();
//        SpringApplication.run(DroolsApplication.class, args);
    }

    public static void testEnquiry() throws Exception {
        Order order = new Order();
        order.setId(1);
        order.setTotalAmount(99);
        Coupon coupon = new Coupon();
        coupon.setCouponBatchCode("SAVE8");
        coupon.setCouponType("4");    // discount
        coupon.setSubCouponType("2"); // 1: order; 2: sku
        coupon.setMinBuyAmount(0);
        coupon.setNominal(80);
        CouponBatchService.addCouponBatch(coupon);

        EnquiryResult result = new EnquiryResult();

        // sku
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(1, 10, 10, 2));
        OrderService.addOrderDetail(order.getId(), items);

        // 在生成环境中，可通过读取数据库来得到脚本内容，从而实现动态加载
        runByReadFile("D:\\xw\\learn\\coupon-groovy\\src\\main\\java\\com\\mall\\coupon\\groovy\\coupon.groovy",
                "enquiry", new Object[]{order, coupon, result});
    }

    public static void runScriptByEngine(String path, String fn) throws Exception {
        // 如果不是 static 方法，使用 this.getClass().getClassLoader()
        GroovyScriptEngine scriptEngine = new GroovyScriptEngine(path, CouponGroovyApplication.class.getClassLoader());
        Binding binding = new Binding();
        binding.setVariable("args", new String[]{"a", "b"});
        Object result1 = scriptEngine.run(fn, binding);
        if(null != result1) {
            log.info("{}", result1);
        } else {
            log.info("null output");
        }
        Coupon coupon = UserCouponService.getUserCouponByCode("SAVE8");
        log.info("coupon SAVE8: {}", coupon);

//       GroovyScriptEngineFactory scriptEngineFactory = new GroovyScriptEngineFactory();
    }

    //
    public static void runByReadFile(String fn, String method, Object o) throws Exception {
        try {
             BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fn));
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (; ; ) {
                int i = bis.read();
                if (i == -1) {
                    break;
                }
                bos.write(i);
            }

            runScript(bos.toString(), method, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runScript(String script, String method, Object o) throws Exception {
        // classLoader 使用完成后自动 close
        try (GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader())) {
            Class klass = classLoader.parseClass(script);
            GroovyObject instance = (GroovyObject) klass.newInstance();

            instance.invokeMethod(method, o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    public static void runByLoadClassFile(String fn, String method, Object o) throws Exception {
        GroovyClassLoader classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        File sourceFile = new File(fn);
        Class klass = classLoader.parseClass(new GroovyCodeSource(sourceFile));
        GroovyObject instance = (GroovyObject)klass.newInstance();

        instance.invokeMethod(method, o);
    }

}
