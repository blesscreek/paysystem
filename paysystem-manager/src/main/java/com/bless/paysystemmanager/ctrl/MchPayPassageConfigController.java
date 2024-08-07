package com.bless.paysystemmanager.ctrl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bless.paysystemcore.aop.MethodLog;
import com.bless.paysystemcore.constants.ApiCodeEnum;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.entity.MchInfo;
import com.bless.paysystemcore.entity.MchPayPassage;
import com.bless.paysystemcore.entity.PayWay;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemcore.model.ApiPageRes;
import com.bless.paysystemcore.model.ApiRes;
import com.bless.paysystemservice.impl.MchAppService;
import com.bless.paysystemservice.impl.MchInfoService;
import com.bless.paysystemservice.impl.MchPayPassageService;
import com.bless.paysystemservice.impl.PayWayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description 商户支付通道管理类
 * @Date 2024-08-06 21:03
 */
@Api(tags = "商户支付通道管理")
@RestController
@RequestMapping("/api/mch/payPassages")
public class MchPayPassageConfigController extends CommonCtrl{
    @Autowired
    private PayWayService payWayService;
    @Autowired
    private MchPayPassageService mchPayPassageService;
    @Autowired
    private MchAppService mchAppService;
    @Autowired
    private MchInfoService mchInfoService;

    @ApiOperation("查询支付方式列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "pageNumber", value = "分页页码", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "appId", value = "应用ID", required = true),
            @ApiImplicitParam(name = "wayCode", value = "支付方式代码"),
            @ApiImplicitParam(name = "wayName", value = "支付方式名称")
    })
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_PASSAGE_LIST')")
    @GetMapping
    public ApiPageRes<PayWay> list() {

        String appId = getValStringRequired("appId");
        String wayCode = getValString("wayCode");
        String wayName = getValString("wayName");

        //支付方式集合
        LambdaQueryWrapper<PayWay> wrapper = PayWay.gw();
        if (StrUtil.isNotBlank(wayCode)) {
            wrapper.eq(PayWay::getWayCode, wayCode);
        }
        if (StrUtil.isNotBlank(wayName)) {
            wrapper.like(PayWay::getWayName, wayName);
        }
        IPage<PayWay> payWayPage = payWayService.page(getIPage(), wrapper);

        if (!CollectionUtils.isEmpty(payWayPage.getRecords())) {

            // 支付方式代码集合
            List<String> wayCodeList = new LinkedList<>();
            payWayPage.getRecords().stream().forEach(payWay -> wayCodeList.add(payWay.getWayCode()));

            // 应用支付通道集合
            List<MchPayPassage> mchPayPassageList = mchPayPassageService.list(MchPayPassage.gw()
                    .select(MchPayPassage::getWayCode, MchPayPassage::getState)
                    .eq(MchPayPassage::getAppId, appId)
                    .in(MchPayPassage::getWayCode, wayCodeList));

            for (PayWay payWay : payWayPage.getRecords()) {
                payWay.addExt("passageState", CS.NO);
                for (MchPayPassage mchPayPassage : mchPayPassageList) {
                    // 某种支付方式多个通道的情况下，只要有一个通道状态为开启，则该支付方式对应为开启状态
                    if (payWay.getWayCode().equals(mchPayPassage.getWayCode()) && mchPayPassage.getState() == CS.YES) {
                        payWay.addExt("passageState", CS.YES);
                        break;
                    }
                }
            }
        }

        return ApiPageRes.pages(payWayPage);
    }
    @ApiOperation("根据[应用ID]、[支付方式代码]查询可用的支付接口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "appId", value = "应用ID", required = true),
            @ApiImplicitParam(name = "wayCode", value = "支付方式代码", required = true)
    })
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_PASSAGE_CONFIG')")
    @GetMapping("/availablePayInterface/{appId}/{wayCode}")
    public ApiRes availablePayInterface(@PathVariable("appId") String appId, @PathVariable("wayCode") String wayCode) {

        MchApp mchApp = mchAppService.getById(appId);
        if (mchApp == null || mchApp.getState() != CS.YES) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        MchInfo mchInfo = mchInfoService.getById(mchApp.getMchNo());
        if (mchInfo == null || mchInfo.getState() != CS.YES) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        // 根据支付方式查询可用支付接口列表
        List<JSONObject> list = mchPayPassageService.selectAvailablePayInterfaceList(wayCode, appId, CS.INFO_TYPE_MCH_APP, mchInfo.getType());

        return ApiRes.ok(list);
    }

    /**
     * @Description: 应用支付通道配置
     */
    @ApiOperation("更新商户支付通道")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "reqParams", value = "商户支付通道配置信息", required = true)
    })
    @PreAuthorize("hasAuthority('ENT_MCH_PAY_PASSAGE_ADD')")
    @PostMapping
    @MethodLog(remark = "更新商户支付通道")
    public ApiRes saveOrUpdate() {

        String reqParams = getValStringRequired("reqParams");

        try {
            List<MchPayPassage> mchPayPassageList = JSONArray.parseArray(reqParams, MchPayPassage.class);
            if (CollectionUtils.isEmpty(mchPayPassageList)) {
                throw new BizException("操作失败");
            }
            MchApp mchApp = mchAppService.getById(mchPayPassageList.get(0).getAppId());
            if (mchApp == null || mchApp.getState() != CS.YES) {
                return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
            }

            mchPayPassageService.saveOrUpdateBatchSelf(mchPayPassageList, mchApp.getMchNo());
            return ApiRes.ok();
        }catch (Exception e) {
            return ApiRes.fail(ApiCodeEnum.SYSTEM_ERROR);
        }
    }
}
