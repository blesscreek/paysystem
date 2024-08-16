package com.bless.paysystempayment.ctrl.payorder;

import com.alibaba.fastjson.JSONObject;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.entity.PayOrder;
import com.bless.paysystemcore.entity.PayWay;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemcore.model.ApiRes;
import com.bless.paysystemcore.utils.Kit;
import com.bless.paysystempayment.model.MchAppConfigContext;
import com.bless.paysystempayment.rqrs.AbstractMchAppRQ;
import com.bless.paysystempayment.rqrs.AbstractRQ;
import com.bless.paysystempayment.rqrs.payOrder.UnifiedOrderRQ;
import com.bless.paysystempayment.rqrs.payOrder.UnifiedOrderRS;
import com.bless.paysystempayment.service.ConfigContextQueryService;
import com.bless.paysystempayment.service.ConfigContextService;
import com.bless.paysystemservice.impl.PayWayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bless
 * @Version 1.0
 * @Description 统一下单 controller
 * @Date 2024-08-10 11:05
 */
@Slf4j
@RestController
public class UnifiedOrderController extends AbstractPayOrderController{
    @Autowired
    private ConfigContextQueryService configContextQueryService;
    @Autowired
    private PayWayService payWayService;

    /**
     * 统一下单接口
     * **/
    @PostMapping("/api/pay/unifiedOrder")
    public ApiRes unifiedOrder() {
        //获取参数 & 验签
        UnifiedOrderRQ rq = getRQByWithMchSign(UnifiedOrderRQ.class);

        UnifiedOrderRQ bizRQ = buildBizRQ(rq);

        //实现子类的res
        ApiRes apiRes = unifiedOrder(bizRQ.getWayCode(), bizRQ);
        if(apiRes.getData() == null){
            return apiRes;
        }

        UnifiedOrderRS bizRes = (UnifiedOrderRS)apiRes.getData();

        //聚合接口，返回的参数
        UnifiedOrderRS res = new UnifiedOrderRS();
        BeanUtils.copyProperties(bizRes, res);

        //只有 订单生成（QR_CASHIER） || 支付中 || 支付成功返回该数据
        if(bizRes.getOrderState() != null && (bizRes.getOrderState() == PayOrder.STATE_INIT || bizRes.getOrderState() == PayOrder.STATE_ING || bizRes.getOrderState() == PayOrder.STATE_SUCCESS) ){
            res.setPayDataType(bizRes.buildPayDataType());
            res.setPayData(bizRes.buildPayData());
        }

        return ApiRes.okWithSign(res, configContextQueryService.queryMchApp(rq.getMchNo(), rq.getAppId()).getAppSecret());


    }
    /** 获取请求参数并转换为对象，商户通用验证  **/
    protected <T extends AbstractRQ> T getRQByWithMchSign(Class<T> cls){

        //获取请求RQ, and 通用验证
        T bizRQ = getRQ(cls);

        AbstractMchAppRQ abstractMchAppRQ = (AbstractMchAppRQ)bizRQ;

        //业务校验， 包括： 验签， 商户状态是否可用， 是否支持该支付方式下单等。
        String mchNo = abstractMchAppRQ.getMchNo();
        String appId = abstractMchAppRQ.getAppId();
        String sign = bizRQ.getSign();

        if(StringUtils.isAnyBlank(mchNo, appId, sign)){
            throw new BizException("参数有误！");
        }

        MchAppConfigContext mchAppConfigContext = configContextQueryService.queryMchInfoAndAppInfo(mchNo, appId);
        if(mchAppConfigContext == null){
            throw new BizException("商户或商户应用不存在");
        }

        if(mchAppConfigContext.getMchInfo() == null || mchAppConfigContext.getMchInfo().getState() != CS.YES){
            throw new BizException("商户信息不存在或商户状态不可用");
        }

        MchApp mchApp = mchAppConfigContext.getMchApp();
        if(mchApp == null || mchApp.getState() != CS.YES){
            throw new BizException("商户应用不存在或应用状态不可用");
        }

        if(!mchApp.getMchNo().equals(mchNo)){
            throw new BizException("参数appId与商户号不匹配");
        }

        // 验签
        String appSecret = mchApp.getAppSecret();

        // 转换为 JSON
        JSONObject bizReqJSON = (JSONObject)JSONObject.toJSON(bizRQ);
        bizReqJSON.remove("sign");
        if(!sign.equalsIgnoreCase(Kit.getSign(bizReqJSON, appSecret))){
            throw new BizException("验签失败");
        }

        return bizRQ;
    }
    private UnifiedOrderRQ buildBizRQ(UnifiedOrderRQ rq) {
        //支付方式  比如： ali_bar
        String wayCode = rq.getWayCode();

        //jsapi 收银台聚合支付场景（不校验是否存在payWayCode）
        if (CS.PAY_WAY_CODE.QR_CASHIER.equals(wayCode)) {
            return rq.buildBizRQ();
        }
//        //如果是自动分类条码
//        if(CS.PAY_WAY_CODE.AUTO_BAR.equals(wayCode)){
//
//            AutoBarOrderRQ bizRQ = (AutoBarOrderRQ)rq.buildBizRQ();
//            wayCode = JeepayKit.getPayWayCodeByBarCode(bizRQ.getAuthCode());
//            rq.setWayCode(wayCode.trim());
//        }
        if (payWayService.count(PayWay.gw().eq(PayWay::getWayCode, wayCode)) <= 0) {
            throw new BizException("不支持的支付方式");
        }
        //转换为bizRQ
        return rq.buildBizRQ();

    }
}
