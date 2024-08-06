package com.bless.paysystemmanager.ctrl.merchant;

import com.bless.paysystemcomponentsmq.model.ResetIsvMchAppInfoConfigMQ;
import com.bless.paysystemcomponentsmq.vender.IMQSender;
import com.bless.paysystemcore.aop.MethodLog;
import com.bless.paysystemcore.constants.ApiCodeEnum;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.entity.MchInfo;
import com.bless.paysystemcore.entity.PayInterfaceConfig;
import com.bless.paysystemcore.entity.PayInterfaceDefine;
import com.bless.paysystemcore.model.ApiRes;
import com.bless.paysystemcore.model.params.NormalMchParams;
import com.bless.paysystemcore.utils.StringKit;
import com.bless.paysystemmanager.ctrl.CommonCtrl;
import com.bless.paysystemservice.impl.MchAppService;
import com.bless.paysystemservice.impl.MchInfoService;
import com.bless.paysystemservice.impl.PayInterfaceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description 商户支付接口配置类
 * @Date 2024-08-05 10:31
 */
@Api(tags = "商户支付接口管理")
@RestController
@RequestMapping("/api/mch/payConfigs")
public class MchPayInterfaceConfigController extends CommonCtrl {
    @Autowired
    private MchAppService mchAppService;
    @Autowired
    private MchInfoService mchInfoService;
    @Autowired
    private PayInterfaceConfigService payInterfaceConfigService;
    @Autowired
    private IMQSender mqSender;

    @ApiOperation("查询应用支付接口配置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "appId", value = "应用ID", required = true)
    })
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_CONFIG_LIST')")
    @GetMapping
    public ApiRes<List<PayInterfaceDefine>> list() {
        List<PayInterfaceDefine> list = payInterfaceConfigService.selectAllPayIfConfigListByAppId(getValStringRequired("appId"));
        return ApiRes.ok(list);
    }
    @ApiOperation("根据应用ID、接口类型 获取应用参数配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "appId", value = "应用ID", required = true),
            @ApiImplicitParam(name = "ifCode", value = "接口类型代码", required = true)
    })
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_CONFIG_VIEW')")
    @GetMapping("/{appId}/{ifCode}")
    public ApiRes getByAppId(@PathVariable(value = "appId") String appId, @PathVariable(value = "ifCode") String ifCode) {
        PayInterfaceConfig payInterfaceConfig = payInterfaceConfigService.getByInfoIdAndIfCode(CS.INFO_TYPE_MCH_APP, appId, ifCode);
        if (payInterfaceConfig != null) {
            // 费率转换为百分比数值
            if (payInterfaceConfig.getIfRate() != null) {
                payInterfaceConfig.setIfRate(payInterfaceConfig.getIfRate().multiply(new BigDecimal("100")));
            }

            // 敏感数据脱敏
            if (StringUtils.isNotBlank(payInterfaceConfig.getIfParams())) {
                MchApp mchApp = mchAppService.getById(appId);
                MchInfo mchInfo = mchInfoService.getById(mchApp.getMchNo());

                // 普通商户的支付参数执行数据脱敏
                if (mchInfo.getType() == CS.MCH_TYPE_NORMAL) {
                    NormalMchParams mchParams = NormalMchParams.factory(payInterfaceConfig.getIfCode(), payInterfaceConfig.getIfParams());
                    if (mchParams != null) {
                        payInterfaceConfig.setIfParams(mchParams.deSenData());
                    }
                }
            }
        }
        return ApiRes.ok(payInterfaceConfig);
    }
    @ApiOperation("更新应用支付参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "infoId", value = "应用AppId", required = true),
            @ApiImplicitParam(name = "ifCode", value = "接口类型代码", required = true)
    })
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_CONFIG_ADD')")
    @PostMapping
    @MethodLog(remark = "更新应用支付参数")
    public ApiRes saveOrUpdate() {
        String infoId = getValStringRequired("infoId");
        String ifCode = getValStringRequired("ifCode");
        MchApp mchApp = mchAppService.getById(infoId);
        if (mchApp == null || mchApp.getState() != CS.YES) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        PayInterfaceConfig payInterfaceConfig = getObject(PayInterfaceConfig.class);
        payInterfaceConfig.setInfoType(CS.INFO_TYPE_MCH_APP);
        payInterfaceConfig.setInfoId(infoId);

        //存入真实费率
        if (payInterfaceConfig.getIfRate() != null) {
            payInterfaceConfig.setIfRate(payInterfaceConfig.getIfRate().divide(new BigDecimal("100"),
                    6, BigDecimal.ROUND_HALF_UP));
        }
        //添加更新者信息
        Long userId = getCurrentUser().getSysUser().getSysUserId();
        String realName = getCurrentUser().getSysUser().getRealname();
        payInterfaceConfig.setUpdatedUid(userId);
        payInterfaceConfig.setUpdatedBy(realName);

        //根据 商户号、接口类型 获取商户参数配置
        PayInterfaceConfig dbRecoed = payInterfaceConfigService.getByInfoIdAndIfCode(CS.INFO_TYPE_MCH_APP, infoId, ifCode);

        if (dbRecoed != null) {
            payInterfaceConfig.setId(dbRecoed.getId());

            //合并支付参数
            payInterfaceConfig.setIfParams(StringKit.marge(dbRecoed.getIfParams(), payInterfaceConfig.getIfParams()));

        } else {
            payInterfaceConfig.setCreatedUid(userId);
            payInterfaceConfig.setCreatedBy(realName);
        }

        boolean result = payInterfaceConfigService.saveOrUpdate(payInterfaceConfig);

        if (!result) {
            return ApiRes.fail(ApiCodeEnum.SYSTEM_ERROR, "配置失败");
        }

        mqSender.send(ResetIsvMchAppInfoConfigMQ.build(ResetIsvMchAppInfoConfigMQ.RESET_TYPE_MCH_APP,null,mchApp.getMchNo(), infoId));
        return ApiRes.ok();

    }






}
