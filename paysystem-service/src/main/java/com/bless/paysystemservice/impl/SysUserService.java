package com.bless.paysystemservice.impl;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.entity.SysUserAuth;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemservice.mapper.SysUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author bless
 * @since 2024-07-30
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    @Autowired
    private SysUserAuthService sysUserAuthService;

    /** 添加系统用户 **/
    @Transactional
    public void addSysUser(SysUser sysUser, String sysType){

        //判断获取到选择的角色集合
//        String roleIdListStr = sysUser.extv().getString("roleIdListStr");
//        if(StringKit.isEmpty(roleIdListStr)) throw new BizException("请选择角色信息！");
//
//        List<String> roleIdList = JSONArray.parseArray(roleIdListStr, String.class);
//        if(roleIdList.isEmpty()) throw new BizException("请选择角色信息！");

        // 判断数据来源
        if( StringUtils.isEmpty(sysUser.getLoginUsername()) ) {
            throw new BizException("登录用户名不能为空！");
        }
        if( StringUtils.isEmpty(sysUser.getRealname()) ) {
            throw new BizException("姓名不能为空！");
        }
        if( StringUtils.isEmpty(sysUser.getTelphone()) ) {
            throw new BizException("手机号不能为空！");
        }
        if(sysUser.getSex() == null ) {
            throw new BizException("性别不能为空！");
        }

        //登录用户名不可重复
        if( count(SysUser.gw().eq(SysUser::getSysType, sysType).eq(SysUser::getLoginUsername, sysUser.getLoginUsername())) > 0 ){
            throw new BizException("登录用户名已存在！");
        }
        //手机号不可重复
        if( count(SysUser.gw().eq(SysUser::getSysType, sysType).eq(SysUser::getTelphone, sysUser.getTelphone())) > 0 ){
            throw new BizException("手机号已存在！");
        }
        //员工号不可重复
        if( count(SysUser.gw().eq(SysUser::getSysType, sysType).eq(SysUser::getUserNo, sysUser.getUserNo())) > 0 ){
            throw new BizException("员工号已存在！");
        }

        //女  默认头像
        if(sysUser.getSex() != null && CS.SEX_FEMALE == sysUser.getSex()){
            sysUser.setAvatarUrl("https://jeequan.oss-cn-beijing.aliyuncs.com/jeepay/img/defava_f.png");
        }else{
            sysUser.setAvatarUrl("https://jeequan.oss-cn-beijing.aliyuncs.com/jeepay/img/defava_m.png");
        }

        //1. 插入用户主表
        sysUser.setSysType(sysType); // 系统类型
        this.save(sysUser);

        Long sysUserId = sysUser.getSysUserId();

        //添加到 user_auth表
        String authPwd = CS.DEFAULT_PWD;

        sysUserAuthService.addUserAuthDefault(sysUserId, sysUser.getLoginUsername(), sysUser.getTelphone(), authPwd, sysType);

        //3. 添加用户角色信息
        //saveUserRole(sysUser.getSysUserId(), new ArrayList<>());

    }

}
