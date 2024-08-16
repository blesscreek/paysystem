package com.bless.paysystempayment.channel.alipay;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.PayOrder;
import com.bless.paysystempayment.channel.AbstractPaymentService;
import com.bless.paysystempayment.model.MchAppConfigContext;
import com.bless.paysystempayment.rqrs.AbstractRS;
import com.bless.paysystempayment.rqrs.payOrder.UnifiedOrderRQ;
import com.bless.paysystempayment.util.PaywayUtil;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * * 支付接口： 支付宝官方
 * * 支付方式： 自适应
 * @Date 2024-08-12 11:34
 */
@Service
public class AlipayPaymentService extends AbstractPaymentService {
    @Override
    public String getIfCode() {
        return CS.IF_CODE.ALIPAY;
    }

    @Override
    public boolean isSupport(String wayCode) {
        return true;
    }

    @Override
    public String preCheck(UnifiedOrderRQ rq, PayOrder payOrder) {
        return PaywayUtil.getRealPaywayService(this, payOrder.getWayCode()).preCheck(rq, payOrder);
    }

    @Override
    public AbstractRS pay(UnifiedOrderRQ rq, PayOrder payOrder, MchAppConfigContext mchAppConfigContext) throws Exception {
        return PaywayUtil.getRealPaywayService(this, payOrder.getWayCode()).pay(rq, payOrder, mchAppConfigContext);
    }


}
