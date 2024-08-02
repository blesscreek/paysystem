package com.bless.paysystemservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysEntitlement;
import com.bless.paysystemcore.entity.SysRoleEntRela;
import com.bless.paysystemservice.mapper.SysRoleEntRelaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description 系统角色权限关联表 服务实现类
 * @Date 2024-08-01 17:33
 */
@Service
public class SysRoleEntRelaService extends ServiceImpl<SysRoleEntRelaMapper, SysRoleEntRela> {
    @Autowired
    private SysEntitlementService sysEntitlementService;


    /** 根据人查询出所有权限ID集合  */
    public List<String> selectEntIdsByUserId(Long userId, Byte isAdmin, String sysType) {
        //判断是否为超管，超管的话就有全部权限
        if (isAdmin == CS.YES) {
            List<String> result = new ArrayList<>();
            sysEntitlementService.list(SysEntitlement.gw().select(SysEntitlement::getEntId).eq(SysEntitlement::getSysType, sysType).eq(SysEntitlement::getState, CS.PUB_USABLE))
                    .stream().forEach(r->result.add(r.getEntId()));
            return result;
        } else {
            return  baseMapper.selectEntIdsByUserId(userId, sysType);
        }
    }
}
