package com.bless.paysystempayment.channel.alipay.payway;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.PayOrder;
import com.bless.paysystemcore.utils.AmountUtil;
import com.bless.paysystempayment.channel.alipay.AlipayKit;
import com.bless.paysystempayment.channel.alipay.AlipayPaymentService;
import com.bless.paysystempayment.model.MchAppConfigContext;
import com.bless.paysystempayment.rqrs.AbstractRS;
import com.bless.paysystempayment.rqrs.msg.ChannelRetMsg;
import com.bless.paysystempayment.rqrs.payOrder.UnifiedOrderRQ;
import com.bless.paysystempayment.rqrs.payOrder.payway.AliQrOrderRQ;
import com.bless.paysystempayment.rqrs.payOrder.payway.AliQrOrderRS;
import com.bless.paysystempayment.service.ConfigContextQueryService;
import com.bless.paysystempayment.util.ApiResBuilder;
import com.bless.paysystemservice.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description 支付宝 QR支付
 * @Date 2024-08-12 12:01
 */
@Service("alipayPaymentByAliQrService") //Service Name需保持全局唯一性
public class AliQr extends AlipayPaymentService {
    @Autowired
    private ConfigContextQueryService configContextQueryService;
    @Autowired
    private SysConfigService sysConfigService;
    @Override
    public String preCheck(UnifiedOrderRQ rq, PayOrder payOrder) {
        return null;
    }

    @Override
    public AbstractRS pay(UnifiedOrderRQ rq, PayOrder payOrder, MchAppConfigContext mchAppConfigContext) {

        AliQrOrderRQ aliQrOrderRQ = (AliQrOrderRQ) rq;

        AlipayTradePrecreateRequest req = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        model.setOutTradeNo(payOrder.getPayOrderId());
        model.setSubject(payOrder.getSubject()); //订单标题
        model.setBody(payOrder.getBody()); //订单描述信息
        model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));  //支付金额
        model.setTimeExpire(DateUtil.format(payOrder.getExpiredTime(), DatePattern.NORM_DATETIME_FORMAT));  // 订单超时时间
        req.setNotifyUrl(getNotifyUrl()); // 设置异步通知地址
        req.setBizModel(model);

//        //统一放置 isv接口必传信息
//        AlipayKit.putApiIsvInfo(mchAppConfigContext, req, model);

        //调起支付宝 （如果异常， 将直接跑出   ChannelException ）
        AlipayTradePrecreateResponse alipayResp = configContextQueryService.getAlipayClientWrapper(mchAppConfigContext).execute(req);


        // 构造函数响应数据
        AliQrOrderRS res = ApiResBuilder.buildSuccess(AliQrOrderRS.class);
        ChannelRetMsg channelRetMsg = new ChannelRetMsg();
        res.setChannelRetMsg(channelRetMsg);

        //放置 响应数据
        channelRetMsg.setChannelAttach(alipayResp.getBody());


        if(alipayResp.isSuccess()){ //处理成功

            if(CS.PAY_DATA_TYPE.CODE_IMG_URL.equals(aliQrOrderRQ.getPayDataType())){ //二维码地址
                res.setCodeImgUrl(sysConfigService.getDBApplicationConfig().genScanImgUrl(alipayResp.getQrCode()));

            }else{ //默认都为跳转地址方式
                res.setCodeUrl(alipayResp.getQrCode());
            }

            channelRetMsg.setChannelState(ChannelRetMsg.ChannelState.WAITING);

        }else{  //其他状态, 表示下单失败
            res.setOrderState(PayOrder.STATE_FAIL);  //支付失败
            channelRetMsg.setChannelErrCode(AlipayKit.appendErrCode(alipayResp.getCode(), alipayResp.getSubCode()));
            channelRetMsg.setChannelErrMsg(AlipayKit.appendErrMsg(alipayResp.getMsg(), alipayResp.getSubMsg()));
        }

        return res;

    }

}
