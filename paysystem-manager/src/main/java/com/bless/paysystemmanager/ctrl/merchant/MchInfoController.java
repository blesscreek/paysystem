package com.bless.paysystemmanager.ctrl.merchant;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bless.paysystemcore.aop.MethodLog;
import com.bless.paysystemcore.entity.MchInfo;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.model.ApiPageRes;
import com.bless.paysystemcore.model.ApiRes;
import com.bless.paysystemmanager.ctrl.CommonCtrl;
import com.bless.paysystemservice.impl.MchInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.nio.channels.WritePendingException;

/**
 * @Author bless
 * @Version 1.0
 * @Description 商户管理类
 * @Date 2024-08-02 17:26
 */
@Api("商户基本信息管理")
@RestController
@RequestMapping("/api/mchInfo")
public class MchInfoController extends CommonCtrl {
    @Autowired
    private MchInfoService mchInfoService;
    @ApiOperation("查询商户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "pageNumber", value = "分页页码", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "mchNo", value = "商户号"),
            @ApiImplicitParam(name = "mchName", value = "商户名称"),
            @ApiImplicitParam(name = "isvNo", value = "服务商号"),
            @ApiImplicitParam(name = "state", value = "状态: 0-停用, 1-启用", dataType = "Byte"),
            @ApiImplicitParam(name = "type", value = "类型: 1-普通商户, 2-特约商户(服务商模式)", dataType = "Byte")
    })
    @PreAuthorize("hasAuthority('ENT_MCH_LIST')")
    @GetMapping
    public ApiPageRes<MchInfo> list() {
        MchInfo mchInfo = getObject(MchInfo.class);

        LambdaQueryWrapper<MchInfo> wrapper = MchInfo.gw();
        if (StringUtils.isNotEmpty(mchInfo.getMchNo())) {
            wrapper.eq(MchInfo::getMchNo, mchInfo.getMchNo());
        }
        if (StringUtils.isNotEmpty(mchInfo.getIsvNo())) {
            wrapper.eq(MchInfo::getIsvNo, mchInfo.getIsvNo());
        }
        if (StringUtils.isNotEmpty(mchInfo.getMchName())) {
            wrapper.eq(MchInfo::getMchName, mchInfo.getMchName());
        }
        if (mchInfo.getType() != null) {
            wrapper.eq(MchInfo::getType, mchInfo.getType());
        }
        if (mchInfo.getState() != null) {
            wrapper.eq(MchInfo::getState, mchInfo.getState());
        }
        wrapper.orderByDesc(MchInfo::getCreatedAt);
        IPage<MchInfo> pages = mchInfoService.page(getIPage(), wrapper);
        return ApiPageRes.pages(pages);
    }

    @ApiOperation("新增商户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "mchName", value = "商户名称", required = true),
            @ApiImplicitParam(name = "mchShortName", value = "商户简称", required = true),
            @ApiImplicitParam(name = "loginUserName", value = "登录名", required = true),
            @ApiImplicitParam(name = "isvNo", value = "服务商号，type为2时必填"),
            @ApiImplicitParam(name = "contactName", value = "联系人姓名", required = true),
            @ApiImplicitParam(name = "contactTel", value = "联系人手机号", required = true),
            @ApiImplicitParam(name = "contactEmail", value = "联系人邮箱"),
            @ApiImplicitParam(name = "remark", value = "备注"),
            @ApiImplicitParam(name = "state", value = "状态: 0-停用, 1-启用", dataType = "Byte"),
            @ApiImplicitParam(name = "type", value = "类型: 1-普通商户, 2-特约商户(服务商模式)", dataType = "Byte")
    })
    @PreAuthorize("hasAuthority('ENT_MCH_INFO_ADD')")
    @MethodLog(remark = "新增商户")
    @RequestMapping(value="", method = RequestMethod.POST)
    public ApiRes add() {
        MchInfo mchInfo = getObject(MchInfo.class);
        //获取传入的商户登录名
        String loginUserName = getValStringRequired("loginUserName");
        mchInfo.setMchNo("M" + DateUtil.currentSeconds());
        //当前登录用户信息
        SysUser sysUser = getCurrentUser().getSysUser();
        mchInfo.setCreatedUid(sysUser.getSysUserId());
        mchInfo.setCreatedBy(sysUser.getRealname());

        mchInfoService.addMch(mchInfo, loginUserName);
        return ApiRes.ok();
    }




}
