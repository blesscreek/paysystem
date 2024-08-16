package com.bless.paysystempayment.ctrl;

import com.bless.paysystempayment.model.MchAppConfigContext;
import com.bless.paysystempayment.rqrs.AbstractMchAppRQ;
import com.bless.paysystempayment.rqrs.AbstractRQ;
import com.alibaba.fastjson.JSONObject;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.ctrls.AbstractCtrl;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemcore.utils.Kit;

import com.bless.paysystempayment.service.ConfigContextQueryService;
import com.bless.paysystempayment.service.ValidateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author bless
 * @Version 1.0
 * @Description api 抽象接口， 公共函数
 * @Date 2024-08-10 11:15
 */

public abstract class ApiController extends AbstractCtrl{

    @Autowired private ValidateService validateService;
    @Autowired private ConfigContextQueryService configContextQueryService;
    /** 获取请求参数并转换为对象，通用验证  **/
    protected <T extends AbstractRQ> T getRQ(Class<T> cls){

        T bizRQ = getObject(cls);

        // [1]. 验证通用字段规则
        validateService.validate(bizRQ);

        return bizRQ;
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

}
