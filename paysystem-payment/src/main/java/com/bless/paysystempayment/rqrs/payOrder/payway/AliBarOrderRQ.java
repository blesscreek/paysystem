package com.bless.paysystempayment.rqrs.payOrder.payway;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystempayment.rqrs.payOrder.UnifiedOrderRQ;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author bless
 * @Version 1.0
 * @Description 支付方式： ALI_BAR
 * @Date 2024-08-10 16:10
 */
@Data
public class AliBarOrderRQ extends UnifiedOrderRQ {
    /** 用户 支付条码 **/
    @NotBlank(message = "支付条码不能为空")
    private String authCode;

    /** 构造函数 **/
    public AliBarOrderRQ(){
        this.setWayCode(CS.PAY_WAY_CODE.ALI_BAR); //默认 ali_bar, 避免validate出现问题
    }

}
