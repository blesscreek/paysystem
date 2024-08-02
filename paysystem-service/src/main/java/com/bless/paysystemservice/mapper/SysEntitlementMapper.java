package com.bless.paysystemservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bless.paysystemcore.entity.SysEntitlement;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

/**
 * @Author bless
 * @Version 1.0
 * @Description 系统权限表 Mapper接口
 * @Date 2024-07-31 20:49
 */
public interface SysEntitlementMapper extends BaseMapper<SysEntitlement> {
    Integer userHasLeftMenu(@Param("userId") Long userId, @Param("sysType") String sysType);
}
