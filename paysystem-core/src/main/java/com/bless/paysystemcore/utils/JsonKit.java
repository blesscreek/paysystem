package com.bless.paysystemcore.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author bless
 * @Version 1.0
 * @Description json工具类
 * @Date 2024-07-31 9:48
 */

public class JsonKit {
    public static com.alibaba.fastjson.JSONObject newJson(String key, Object val){

        com.alibaba.fastjson.JSONObject result = new JSONObject();
        result.put(key, val);
        return result;
    }
}
