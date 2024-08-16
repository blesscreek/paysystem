package com.bless.paysystempayment.channel.alipay;

import cn.hutool.core.text.CharSequenceUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author bless
 * @Version 1.0
 * @Description 【支付宝】支付通道工具包
 * @Date 2024-08-12 12:23
 */

public class AlipayKit {
    public static String appendErrCode(String code, String subCode){
        return StringUtils.defaultIfEmpty(subCode, code); //优先： subCode
    }

    public static String appendErrMsg(String msg, String subMsg){

        String result = null;
        if(StringUtils.isNotEmpty(msg) && StringUtils.isNotEmpty(subMsg) ){
            result = msg + "【" + subMsg + "】";
        }else{
            result = StringUtils.defaultIfEmpty(subMsg, msg);
        }
        return CharSequenceUtil.maxLength(result, 253);
    }

}
