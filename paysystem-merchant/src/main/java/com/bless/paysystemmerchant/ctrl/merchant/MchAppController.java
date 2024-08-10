package com.bless.paysystemmerchant.ctrl.merchant;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.model.ApiPageRes;
import com.bless.paysystemmerchant.ctrl.CommonCtrl;
import com.bless.paysystemservice.impl.MchAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-07 16:44
 */
@Api(tags = "商户应用管理")
@RestController
@RequestMapping("/api/mchApps")
public class MchAppController extends CommonCtrl {
    @Autowired
    private MchAppService mchAppService;
    @ApiOperation("查询应用列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header"),
            @ApiImplicitParam(name = "pageNumber", value = "分页页码", dataType = "int", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "分页条数", dataType = "int", defaultValue = "20"),
            @ApiImplicitParam(name = "appId", value = "应用ID"),
            @ApiImplicitParam(name = "appName", value = "应用名称"),
            @ApiImplicitParam(name = "state", value = "状态: 0-停用, 1-启用", dataType = "Byte")
    })
    @PreAuthorize("hasAuthority('ENT_MCH_APP_LIST')")
    @GetMapping
    public ApiPageRes<MchApp> list() {
        MchApp mchApp = getObject(MchApp.class);
        mchApp.setMchNo(getCurrentMchNo());

        IPage<MchApp> pages = mchAppService.selectPage(getIPage(true), mchApp);
        return ApiPageRes.pages(pages);
    }
}
