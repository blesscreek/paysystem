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
    /** ！！不同系统请放置不同的redis库 ！！ **/
    /** 缓存key: 当前用户所有用户的token集合  example: TOKEN_1001_HcNheNDqHzhTIrT0lUXikm7xU5XY4Q */
    public static final String CACHE_KEY_TOKEN = "TOKEN_%s_%s";
    public static String getCacheKeyToken(Long sysUserId, String uuid){
        return String.format(CACHE_KEY_TOKEN, sysUserId, uuid);
    }
    /** yes or no **/
    public static final byte NO = 0;
    public static final byte YES = 1;

    /** 图片验证码 缓存key **/
    public static final String CACHE_KEY_IMG_CODE = "img_code_%s";
    public static String getCacheKeyImgCode(String imgToken){
        return String.format(CACHE_KEY_IMG_CODE, imgToken);
    }

    /** 通用 可用 / 禁用 **/
    public static final int PUB_USABLE = 1;
    public static final int PUB_DISABLE = 0;
    //access_token 名称
    public static final String ACCESS_TOKEN_NAME = "iToken";

    public static final long TOKEN_TIME = 60 * 60 * 2; //单位：s,  两小时
    //菜单类型
    public interface ENT_TYPE{

        String MENU_LEFT = "ML";  //左侧显示菜单
        String MENU_OTHER = "MO";  //其他菜单
        String PAGE_OR_BTN = "PB";  //页面 or 按钮

    }
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
