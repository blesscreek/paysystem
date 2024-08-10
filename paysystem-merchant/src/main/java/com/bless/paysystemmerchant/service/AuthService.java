package com.bless.paysystemmerchant.service;

import cn.hutool.core.util.IdUtil;
import com.bless.paysystemcore.cache.ITokenService;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemcore.exception.MyAuthenticationException;
import com.bless.paysystemcore.jwt.JWTPayload;
import com.bless.paysystemcore.jwt.JWTUtils;
import com.bless.paysystemcore.model.security.MyUserDetails;

import com.bless.paysystemmerchant.config.SystemYmlConfig;
import com.bless.paysystemservice.impl.SysRoleEntRelaService;
import com.bless.paysystemservice.impl.SysRoleService;
import com.bless.paysystemservice.mapper.SysEntitlementMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description 认证Service
 * @Date 2024-08-01 9:53
 */
@Slf4j
@Service
public class AuthService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private SysEntitlementMapper sysEntitlementMapper;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleEntRelaService sysRoleEntRelaService;
    @Autowired
    private SystemYmlConfig systemYmlConfig;

    /**
     * 认证
     */
    public String auth(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);

        //spring-security自动认证过程
        // 1. 进入 MyUserDetailsServiceImpl.loadUserByUsername 获取用户基本信息；
        //2. SS根据UserDetails接口验证是否用户可用；
        //3. 最后返回loadUserByUsername 封装的对象信息；
        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(upToken);
        } catch (MyAuthenticationException e) {
            throw e.getBizException() == null ? new BizException(e.getMessage()) : e.getBizException();
        } catch (BadCredentialsException e) {
            throw new BizException("用户名/密码错误！");
        } catch (AuthenticationException e) {
            log.error("AuthenticationException:", e);
            throw new BizException("认证服务出现异常， 请重试或联系系统管理员！");
        }
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

        //验证通过后 再查询用户角色和权限信息集合
        SysUser sysUser = myUserDetails.getSysUser();
        if (sysUser.getIsAdmin() != CS.YES && sysEntitlementMapper.userHasLeftMenu(sysUser.getSysUserId(), CS.SYS_TYPE.MGR) <= 0) {
            throw new BizException("当前用户未分配任何菜单权限，请联系管理员进行分配后再登录！");
        }
        //放置权限集合
        myUserDetails.setAuthorities(getUserAuthority(sysUser));
        //生成token
        String cacheKeyToken = CS.getCacheKeyToken(sysUser.getSysUserId(), IdUtil.fastUUID());
        //生成iToken 并放置到缓存
        //把cacheKey作为键，userDetails作为值存入
        ITokenService.processTokenCache(myUserDetails, cacheKeyToken);//处理token缓存信息
        //将信息放置到Spring-security context中
        UsernamePasswordAuthenticationToken authenticationRest = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationRest);

        //返回JWTToken
        return JWTUtils.generateToken(new JWTPayload(myUserDetails), systemYmlConfig.getJwtSecret());

    }
    public List<SimpleGrantedAuthority> getUserAuthority(SysUser sysUser) {
        //用户拥有的角色集合， 需要以ROLE_开头，用户拥有的权限集合
        List<String> roleList = sysRoleService.findListByUser(sysUser.getSysUserId());
        List<String> entList = sysRoleEntRelaService.selectEntIdsByUserId(sysUser.getSysUserId(), sysUser.getIsAdmin(), sysUser.getSysType());

        List<SimpleGrantedAuthority> grantedAuthorities = new LinkedList<>();
        roleList.stream().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role)));
        entList.stream().forEach(ent-> grantedAuthorities.add(new SimpleGrantedAuthority(ent)));
        return grantedAuthorities;

    }

}
