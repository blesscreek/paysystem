package com.bless.paysystemservice.impl;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysUserAuth;
import com.bless.paysystemcore.utils.StringKit;
import com.bless.paysystemservice.mapper.SysUserAuthMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统用户认证表 服务实现类
 * </p>
 *
 * @author bless
 * @since 2024-07-30
 */
@Service
public class SysUserAuthService extends ServiceImpl<SysUserAuthMapper, SysUserAuth>  {

    public SysUserAuth selectByLogin(String identifier, Byte identityType, String sysType) {
        return baseMapper.selectByLogin(identifier, identityType, sysType);
    }
    /** 添加用户认证表 **/
    @Transactional
    public void addUserAuthDefault(Long userId, String loginUserName, String telPhone, String pwdRaw, String sysType){

        String salt = StringKit.getUUID(6); //6位随机数
        String userPwd = new BCryptPasswordEncoder().encode(pwdRaw);

        /** 用户名登录方式 */
        SysUserAuth record = new SysUserAuth();
        record.setUserId(userId);
        record.setCredential(userPwd);
        record.setSalt(salt);
        record.setSysType(sysType);
        record.setIdentityType(CS.AUTH_TYPE.LOGIN_USER_NAME);
        record.setIdentifier(loginUserName);
        save(record);

        /** 手机号登录方式 */
        record = new SysUserAuth();
        record.setUserId(userId);
        record.setCredential(userPwd);
        record.setSalt(salt);
        record.setSysType(sysType);
        record.setIdentityType(CS.AUTH_TYPE.TELPHONE);
        record.setIdentifier(telPhone);
        save(record);
    }
}
