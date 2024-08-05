package com.bless.paysystemcore.model.params;

import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.util.StrUtil;

/**
 * @Author bless
 * @Version 1.0
 * @Description 普通商户参数定义
 * @Date 2024-08-05 15:30
 */

public abstract class NormalMchParams {
    public static NormalMchParams factory(String ifCode, String paramsStr){
        try{
            return (NormalMchParams) JSONObject.parseObject(paramsStr,
                    Class.forName(NormalMchParams.class.getPackage().getName() + "."
                    + ifCode + "." + StrUtil.upperFirst(ifCode) +"NormalMchParams"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 敏感数据脱敏
     */
    public abstract String deSenData();
}
