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

    //接口类型
    public interface IF_CODE{

        String ALIPAY = "alipay";   // 支付宝官方支付
        String WXPAY = "wxpay";     // 微信官方支付
        String YSFPAY = "ysfpay";   // 云闪付开放平台
        String XXPAY = "xxpay";     // 小新支付
        String PPPAY = "pppay";     // Paypal 支付
        String PLSPAY = "plspay";     // 计全支付plus
    }

    //支付数据包 类型
    public interface PAY_DATA_TYPE {
        String PAY_URL = "payurl";  //跳转链接的方式  redirectUrl
        String FORM = "form";  //表单提交
        String WX_APP = "wxapp";  //微信app参数
        String ALI_APP = "aliapp";  //支付宝app参数
        String YSF_APP = "ysfapp";  //云闪付app参数
        String CODE_URL = "codeUrl";  //二维码URL
        String CODE_IMG_URL = "codeImgUrl";  //二维码图片显示URL
        String NONE = "none";  //无参数
//        String QR_CONTENT = "qrContent";  //二维码实际内容
    }

    //支付方式代码
    public interface PAY_WAY_CODE{

        // 特殊支付方式
        String QR_CASHIER = "QR_CASHIER"; //  ( 通过二维码跳转到收银台完成支付， 已集成获取用户ID的实现。  )
        String AUTO_BAR = "AUTO_BAR"; // 条码聚合支付（自动分类条码类型）
        String ALI_BAR = "ALI_BAR";  //支付宝条码支付
        String ALI_QR = "ALI_QR";  //支付宝 二维码付款
        String ALI_OC = "ALI_OC";  //支付宝 订单码支付


        String WX_JSAPI = "WX_JSAPI";  //微信jsapi支付
        String WX_LITE = "WX_LITE";  //微信小程序支付
        String WX_BAR = "WX_BAR";  //微信条码支付
        String WX_H5 = "WX_H5";  //微信H5支付
        String WX_NATIVE = "WX_NATIVE";  //微信扫码支付

    }
    /**
     * 账号类型:1-服务商 2-商户 3-商户应用
     */
    public static final byte INFO_TYPE_ISV = 1;
    public static final byte INFO_TYPE_MCH = 2;
    public static final byte INFO_TYPE_MCH_APP = 3;
    /** 默认密码 */
    public static final String DEFAULT_PWD = "bless123";

    /**
     * 性别 1- 男， 2-女
     */
    public static final byte SEX_UNKNOWN = 0;
    public static final byte SEX_MALE = 1;
    public static final byte SEX_FEMALE = 2;
    /**
     * 商户类型:1-普通商户 2-特约商户
     */
    public static final byte MCH_TYPE_NORMAL = 1;
    public static final byte MCH_TYPE_ISVSUB = 2;
    //登录图形验证码缓存时间，单位：s
    public static final int VERCODE_CACHE_TIME = 60 * 60 * 24;
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

    public static final long TOKEN_TIME = 60 * 60 * 24; //单位：s,  两小时
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
