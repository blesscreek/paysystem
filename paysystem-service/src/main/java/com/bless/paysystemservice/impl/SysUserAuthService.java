package com.bless.paysystemservice.impl;

import com.bless.paysystemcore.entity.SysUserAuth;
import com.bless.paysystemservice.mapper.SysUserAuthMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
