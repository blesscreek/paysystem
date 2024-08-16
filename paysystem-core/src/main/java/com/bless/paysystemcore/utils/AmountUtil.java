package com.bless.paysystemcore.utils;

import java.math.BigDecimal;

/**
 * @Author bless
 * @Version 1.0
 * @Description 金额工具类
 * @Date 2024-08-11 14:25
 */

public class AmountUtil {
    /**
     * 计算百分比类型的各种费用值  （订单金额 * 真实费率  结果四舍五入并保留0位小数 ）
     *
     *  @param amount 订单金额  （保持与数据库的格式一致 ，单位：分）
     * @param rate 费率   （保持与数据库的格式一致 ，真实费率值，如费率为0.55%，则传入 0.0055）
     */
    public static Long calPercentageFee(Long amount, BigDecimal rate){
        return calPercentageFee(amount, rate, BigDecimal.ROUND_HALF_UP);
    }
    /**
     * 计算百分比类型的各种费用值  （订单金额 * 真实费率  结果四舍五入并保留0位小数 ）
     *
     * @param amount 订单金额  （保持与数据库的格式一致 ，单位：分）
     * @param rate 费率   （保持与数据库的格式一致 ，真实费率值，如费率为0.55%，则传入 0.0055）
     * @param mode 模式 参考：BigDecimal.ROUND_HALF_UP(四舍五入)   BigDecimal.ROUND_FLOOR（向下取整）
     */
    public static Long calPercentageFee(Long amount, BigDecimal rate, int mode){
        //费率乘以订单金额   结果四舍五入并保留0位小数
        return new BigDecimal(amount).multiply(rate).setScale(0, mode).longValue();
    }
    /**
     * 将字符串"分"转换成"元"（长格式），如：100分被转换为1.00元。
     * @param s
     * @return
     */
    public static String convertCent2Dollar(String s) {
        if("".equals(s) || s ==null){
            return "";
        }
        long l;
        if(s.length() != 0) {
            if(s.charAt(0) == '+') {
                s = s.substring(1);
            }
            l = Long.parseLong(s);
        } else {
            return "";
        }
        boolean negative = false;
        if(l < 0) {
            negative = true;
            l = Math.abs(l);
        }
        s = Long.toString(l);
        if(s.length() == 1) {
            return(negative ? ("-0.0" + s) : ("0.0" + s));
        }
        if(s.length() == 2) {
            return(negative ? ("-0." + s) : ("0." + s));
        } else {
            return(negative ? ("-" + s.substring(0, s.length() - 2) + "." + s
                    .substring(s.length() - 2)) : (s.substring(0,
                    s.length() - 2)
                    + "." + s.substring(s.length() - 2)));
        }
    }


}
