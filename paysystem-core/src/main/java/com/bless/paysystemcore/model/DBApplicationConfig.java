package com.bless.paysystemcore.model;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSONObject;
import com.bless.paysystemcore.utils.Kit;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author bless
 * @Version 1.0
 * @Description 系统应用配置项定义Bean
 * @Date 2024-08-10 9:52
 */
@Data
public class DBApplicationConfig implements Serializable {

    /** 运营系统地址 **/
    private String mgrSiteUrl;

    /** 商户系统地址 **/
    private String mchSiteUrl;

    /** 支付网关地址 **/
    private String paySiteUrl;

    /** oss公共读文件地址 **/
    private String ossPublicSiteUrl;

    /** 生成  【jsapi统一收银台跳转地址】 **/
    public String genUniJsapiPayUrl(String payOrderId){
        return getPaySiteUrl() + "/cashier/index.html#/hub/" + Kit.aesEncode(payOrderId);
    }

    /** 生成  【jsapi统一收银台】oauth2获取用户ID回调地址 **/
    public String genOauth2RedirectUrlEncode(String payOrderId){
        return URLUtil.encodeAll(getPaySiteUrl() + "/cashier/index.html#/oauth2Callback/" + Kit.aesEncode(payOrderId));
    }

    /** 生成  【商户获取渠道用户ID接口】oauth2获取用户ID回调地址 **/
    public String genMchChannelUserIdApiOauth2RedirectUrlEncode(JSONObject param){
        return URLUtil.encodeAll(getPaySiteUrl() + "/api/channelUserId/oauth2Callback/" + Kit.aesEncode(param.toJSONString()));
    }

    /** 生成  【jsapi统一收银台二维码图片地址】 **/
    public String genScanImgUrl(String url){
        return getPaySiteUrl() + "/api/scan/imgs/" + Kit.aesEncode(url) + ".png";
    }

    /** 生成  【支付宝 isv子商户的授权链接地址】 **/
    public String genAlipayIsvsubMchAuthUrl(String isvNo, String mchAppId){
        return getPaySiteUrl() + "/api/channelbiz/alipay/redirectAppToAppAuth/" + isvNo + "_" + mchAppId;
    }

}
