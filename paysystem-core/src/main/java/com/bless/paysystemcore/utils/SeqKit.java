package com.bless.paysystemcore.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author bless
 * @Version 1.0
 * @Description 序列号生成 工具类
 * @Date 2024-08-10 17:16
 */

public class SeqKit {
    /** 是否使用MybatisPlus生成分布式ID **/
    private static final boolean IS_USE_MP_ID = true;
    private static final AtomicLong PAY_ORDER_SEQ = new AtomicLong(0L);
    private static final String PAY_ORDER_SEQ_PREFIX = "P";
    /** 生成支付订单号 **/
    public static String genPayOrderId() {
        if(IS_USE_MP_ID) {
            return PAY_ORDER_SEQ_PREFIX + IdWorker.getIdStr();
        }
        return String.format("%s%s%04d",PAY_ORDER_SEQ_PREFIX,
                DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN),
                (int) PAY_ORDER_SEQ.getAndIncrement() % 10000);
    }

}
