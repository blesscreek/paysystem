package com.bless.paysystemcore.jwt;

import com.alibaba.fastjson.JSONObject;
import com.bless.paysystemcore.model.security.MyUserDetails;
import lombok.Data;

import java.util.Map;

/**
 * @Author bless
 * @Version 1.0
 * @Description JWT payload 载体
 * * 格式：
 *     {
 *         "sysUserId": "10001",
 *         "created": "1568250147846",
 *         "cacheKey": "KEYKEYKEYKEY",
 *     }
 * @Date 2024-07-31 11:47
 */
@Data
public class JWTPayload {
    private Long sysUserId;       //登录用户ID
    private Long created;         //创建时间, 格式：13位时间戳
    private String cacheKey;      //redis保存的key
    protected JWTPayload(){}

    public JWTPayload(MyUserDetails myUserDetails){

        this.setSysUserId(myUserDetails.getSysUser().getSysUserId());
        this.setCreated(System.currentTimeMillis());
        this.setCacheKey(myUserDetails.getCacheKey());
    }


    /** toMap **/
    public Map<String, Object> toMap(){
        JSONObject json = (JSONObject)JSONObject.toJSON(this);
        return json.toJavaObject(Map.class);
    }
}
