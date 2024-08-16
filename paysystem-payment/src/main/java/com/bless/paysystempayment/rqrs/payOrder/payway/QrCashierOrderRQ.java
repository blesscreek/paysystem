package com.bless.paysystempayment.rqrs.payOrder.payway;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystempayment.rqrs.payOrder.CommonPayDataRQ;
import lombok.Data;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-11 14:40
 */
@Data
public class QrCashierOrderRQ extends CommonPayDataRQ {

    /** 构造函数 **/
    public QrCashierOrderRQ(){
        this.setWayCode(CS.PAY_WAY_CODE.QR_CASHIER);
    }
}
