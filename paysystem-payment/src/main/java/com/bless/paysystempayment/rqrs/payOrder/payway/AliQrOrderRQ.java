package com.bless.paysystempayment.rqrs.payOrder.payway;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystempayment.rqrs.payOrder.CommonPayDataRQ;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-12 12:15
 */

public class AliQrOrderRQ extends CommonPayDataRQ {
    /** 构造函数 **/
    public AliQrOrderRQ(){
        this.setWayCode(CS.PAY_WAY_CODE.ALI_QR);
    }
}
