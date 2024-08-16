package com.bless.paysystempayment.model;

import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.entity.MchInfo;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author bless
 * @Version 1.0
 * @Description 商户配置信息
 * * 放置到内存， 避免多次查询操作
 * @Date 2024-08-10 11:50
 */
@Data
public class MchInfoConfigContext {
    /** 商户信息缓存 */
    private String mchNo;
    private Byte mchType;
    private MchInfo mchInfo;
    private Map<String, MchApp> appMap = new ConcurrentHashMap<>();

    /** 重置商户APP **/
    public void putMchApp(MchApp mchApp){
        appMap.put(mchApp.getAppId(), mchApp);
    }

    /** get商户APP **/
    public MchApp getMchApp(String appId){
        return appMap.get(appId);
    }
}
