package com.bless.paysystempayment.rqrs.payOrder;

import lombok.Data;

/**
 * @Author bless
 * @Version 1.0
 * @Description 通用支付数据RQ
 * @Date 2024-08-11 14:36
 */
@Data
public class CommonPayDataRQ extends UnifiedOrderRQ{
    /** 请求参数： 支付数据包类型 **/
    private String payDataType;
}
