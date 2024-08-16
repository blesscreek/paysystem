package com.bless.paysystempayment.util;

import cn.hutool.core.util.StrUtil;
import com.bless.paysystemcore.utils.SpringBeansUtil;
import com.bless.paysystempayment.channel.IPaymentService;

/**
 * @Author bless
 * @Version 1.0
 * @Description 支付方式动态调用Utils
 * @Date 2024-08-12 11:35
 */

public class PaywayUtil {
    private static final String PAYWAY_PACKAGE_NAME = "payway";
    private static final String PAYWAYV3_PACKAGE_NAME = "paywayV3";

    /** 获取真实的支付方式Service **/
    public static IPaymentService getRealPaywayService(Object obj, String wayCode){

        try {

            //下划线转换驼峰 & 首字母大写
            String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(wayCode.toLowerCase()));
            return (IPaymentService) SpringBeansUtil.getBean(
                    Class.forName(obj.getClass().getPackage().getName()
                            + "." + PAYWAY_PACKAGE_NAME
                            + "." + clsName)
            );

        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /** 获取微信V3真实的支付方式Service **/
    public static IPaymentService getRealPaywayV3Service(Object obj, String wayCode){

        try {

            //下划线转换驼峰 & 首字母大写
            String clsName = StrUtil.upperFirst(StrUtil.toCamelCase(wayCode.toLowerCase()));
            return (IPaymentService) SpringBeansUtil.getBean(
                    Class.forName(obj.getClass().getPackage().getName()
                            + "." + PAYWAYV3_PACKAGE_NAME
                            + "." + clsName)
            );

        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
