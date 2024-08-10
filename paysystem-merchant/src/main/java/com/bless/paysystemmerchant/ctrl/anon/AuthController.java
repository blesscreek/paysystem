package com.bless.paysystemmerchant.ctrl.anon;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import com.bless.paysystemcore.aop.MethodLog;
import com.bless.paysystemcore.cache.RedisUtil;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemcore.model.ApiRes;

import com.bless.paysystemmerchant.ctrl.CommonCtrl;
import com.bless.paysystemmerchant.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.lang.UUID;


/**
 * @Author bless
 * @Version 1.0
 * @Description 认证模块
 * @Date 2024-07-31 21:44
 */
@Api(tags = "认证模块")
@RestController
@RequestMapping("/api/anon/auth")
public class AuthController extends CommonCtrl {
    @Autowired
    private AuthService authService;
    @ApiOperation("登录认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ia", value = "用户名 i account, 需要base64处理", required = true),
            @ApiImplicitParam(name = "ip", value = "密码 i passport,  需要base64处理", required = true),
            @ApiImplicitParam(name = "vc", value = "证码 vercode,  需要base64处理", required = true),
            @ApiImplicitParam(name = "vt", value = "验证码token, vercode token ,  需要base64处理", required = true)
    })
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @MethodLog(remark = "登录认证")
    public ApiRes validate() throws BizException {

        String account = Base64.decodeStr(getValStringRequired("ia"));  //用户名 i account, 已做base64处理
        String ipassport = Base64.decodeStr(getValStringRequired("ip"));	//密码 i passport,  已做base64处理
        String vercode = Base64.decodeStr(getValStringRequired("vc"));	//验证码 vercode,  已做base64处理
        String vercodeToken = Base64.decodeStr(getValStringRequired("vt"));	//验证码token, vercode token ,  已做base64处理

        String cacheCode = RedisUtil.getString(CS.getCacheKeyImgCode(vercodeToken));
        if(StringUtils.isEmpty(cacheCode) || !cacheCode.equalsIgnoreCase(vercode)){
            throw new BizException("验证码有误！");
        }

        // 返回前端 accessToken
        String accessToken = authService.auth(account, ipassport);

//        // 删除图形验证码缓存数据
//        RedisUtil.del(CS.getCacheKeyImgCode(vercodeToken));

        return ApiRes.ok4newJson(CS.ACCESS_TOKEN_NAME, accessToken);
    }

    @ApiOperation("图片验证码")
    @RequestMapping(value = "/vercode", method = RequestMethod.GET)
    public ApiRes vercode() {
        //定义图形验证码长和宽//4位验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(137, 40, 4, 80);
        lineCaptcha.createCode();//生成code

        //redis
        String vercodeToken = UUID.fastUUID().toString();
        RedisUtil.setString(CS.getCacheKeyImgCode(vercodeToken), lineCaptcha.getCode(), CS.VERCODE_CACHE_TIME);

        JSONObject result = new JSONObject();
        result.put("imageBase64Data", lineCaptcha.getImageBase64Data());//将验证码图片转换为Base64格式，并存储到JSON对象中。
        result.put("vercodeToken", vercodeToken);
        result.put("expireTime", CS.VERCODE_CACHE_TIME);

        return ApiRes.ok(result);
    }

}
