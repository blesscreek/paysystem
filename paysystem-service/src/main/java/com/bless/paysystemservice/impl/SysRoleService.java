package com.bless.paysystemservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bless.paysystemcore.entity.SysRole;
import com.bless.paysystemcore.entity.SysUserRoleRela;
import com.bless.paysystemservice.mapper.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-01 17:08
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {
    @Autowired
    private SysUserRoleRelaService sysUserRoleRelaService;
    /** 根据用户查询全部角色集合 **/
    public List<String> findListByUser(Long sysUserId) {
        List<String> result = new ArrayList<>();
        sysUserRoleRelaService.list(
                SysUserRoleRela.gw().eq(SysUserRoleRela::getUserId, sysUserId)
        ).stream().forEach(r -> result.add(r.getRoleId()));
        return result;
    }
}
