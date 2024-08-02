package com.bless.paysystemmanager.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.model.ApiRes;
import com.bless.paysystemcore.model.security.MyUserDetails;
import com.bless.paysystemcore.entity.SysEntitlement;
import com.bless.paysystemcore.utils.TreeDataBuilder;
import com.bless.paysystemservice.impl.SysEntitlementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description 当前登录者的信息相关接口
 * @Date 2024-07-31 15:53
 */
@Api(tags = "登录者信息")
@RestController
@RequestMapping("api/current")
public class CurrentUserController extends CommonCtrl{
    @Autowired
    private SysEntitlementService sysEntitlementService;
    @ApiOperation("查询当前登录者的用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iToken", value = "用户身份凭证", required = true, paramType = "header")
    })
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ApiRes currentUserInfo() {
        //当前用户信息
        MyUserDetails myUserDetails = getCurrentUser();
        SysUser user = myUserDetails.getSysUser();

        //1. 当前用户所有权限ID集合
        ArrayList<String> entIdList = new ArrayList<>();
        myUserDetails.getAuthorities().stream().forEach(r->entIdList.add(r.getAuthority()));

        List<SysEntitlement> allMenuList = new ArrayList<>();//所有菜单集合

        //2、查询出用户所有菜单集合 (包含左侧显示菜单 和 其他类型菜单 )
        if (!entIdList.isEmpty()) {
            allMenuList = sysEntitlementService.list(SysEntitlement.gw()
                    .in(SysEntitlement::getEntId, entIdList)
                    .in(SysEntitlement::getEntType, Arrays.asList(CS.ENT_TYPE.MENU_LEFT, CS.ENT_TYPE.MENU_OTHER))
                    .eq(SysEntitlement::getSysType, CS.SYS_TYPE.MGR)
                    .eq(SysEntitlement::getState, CS.PUB_USABLE));
        }
        //4、转换为json树状结构
        JSONArray jsonArray = (JSONArray) JSON.toJSON(allMenuList);
        List<JSONObject> allMenuRouteTree = new TreeDataBuilder(jsonArray,
                "entId", "pid", "children", "entSort", true)
                .buildTreeObject();
        //5、所有权限id集合
        user.addExt("endIdList", entIdList);
        user.addExt("allMenuRouteTree", allMenuRouteTree);
        return ApiRes.ok(getCurrentUser().getSysUser());
    }

}
