package com.bless.paysystempayment.channel;

import com.bless.paysystemcore.entity.PayOrder;
import com.bless.paysystempayment.model.MchAppConfigContext;
import com.bless.paysystempayment.rqrs.payOrder.UnifiedOrderRQ;
import com.bless.paysystemservice.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author bless
 * @Version 1.0
 * @Description 支付接口抽象类
 * @Date 2024-08-12 11:10
 */

public abstract class AbstractPaymentService implements IPaymentService{
    @Autowired
    private SysConfigService sysConfigService;
    @Override
    public String customPayOrderId(UnifiedOrderRQ bizRQ, PayOrder payOrder, MchAppConfigContext mchAppConfigContext){
        return null; //使用系统默认支付订单号
    }

    protected String getNotifyUrl(){
        return sysConfigService.getDBApplicationConfig().getPaySiteUrl() + "/api/pay/notify/" + getIfCode();
    }
}
