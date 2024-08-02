package com.bless.paysystemcore.cache;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.model.security.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-02 10:59
 */

public class ITokenService {
    /** 处理token信息
     * 1. 如果不允许多用户则踢掉之前的所有用户信息
     * 2. 更新token 缓存时间信息
     * 3. 更新用户token列表
     * **/
    public static void processTokenCache(MyUserDetails myUserDetails, String cacheKey) {
        myUserDetails.setCacheKey(cacheKey);
        //保存token
        RedisUtil.set(cacheKey, myUserDetails, CS.TOKEN_TIME); //缓存时间2小时, 保存具体信息而只是uid, 因为很多场景需要得到信息， 例如验证接口权限， 每次请求都需要获取。 将信息封装在一起减少磁盘请求次数， 如果放置多个key会增加非顺序读取。
    }
}
