package com.bless.paysystemcore.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author bless
 * @Version 1.0
 * @Description Constants 常量对象
 * @Date 2024-07-30 15:14
 */

public class CS {
    /** 通用 可用 / 禁用 **/
    public static final int PUB_USABLE = 1;
    public static final int PUB_DISABLE = 0;
    //access_token 名称
    public static final String ACCESS_TOKEN_NAME = "iToken";

    public static final long TOKEN_TIME = 60 * 60 * 2; //单位：s,  两小时
    /** 系统类型定义 **/
    public interface SYS_TYPE{
        String MCH = "MCH";
        String MGR = "MGR";
        Map<String, String> SYS_TYPE_MAP = new HashMap<>();
    }

    /** 登录认证类型 **/
    public interface AUTH_TYPE{

        byte LOGIN_USER_NAME = 1; //登录用户名
        byte TELPHONE = 2; //手机号
        byte EMAIL = 3; //邮箱

        byte WX_UNION_ID = 10; //微信unionId
        byte WX_MINI = 11; //微信小程序
        byte WX_MP = 12; //微信公众号

        byte QQ = 20; //QQ
    }
}
