package com.bless.paysystemmanager.security;

import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.entity.SysUserAuth;
import com.bless.paysystemcore.exception.MyAuthenticationException;
import com.bless.paysystemcore.model.security.MyUserDetails;
import com.bless.paysystemcore.utils.RegKit;
import com.bless.paysystemservice.impl.SysUserAuthService;
import com.bless.paysystemservice.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description 实现UserDetailsService接口
 * @Date 2024-07-30 15:03
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserAuthService sysUserAuthService;
    @Override
    public UserDetails loadUserByUsername(String loginUsernameStr) throws UsernameNotFoundException {
        byte identityType = CS.AUTH_TYPE.LOGIN_USER_NAME;
        if (RegKit.isMobile(loginUsernameStr)) {
            identityType = CS.AUTH_TYPE.TELPHONE;
        }
        SysUserAuth auth = sysUserAuthService.selectByLogin(loginUsernameStr, identityType, CS.SYS_TYPE.MGR);
        if (auth == null) {//没有该用户信息
            throw MyAuthenticationException.build("用户名/密码错误");
        }
        //用户id
        Long userId = auth.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw MyAuthenticationException.build("用户名/密码错误");
        }
        if (CS.PUB_USABLE != sysUser.getState()) {//状态不合法
            throw MyAuthenticationException.build("用户状态不可登录，请联系管理员");
        }
        return new MyUserDetails(sysUser, auth.getCredential());
    }
}
