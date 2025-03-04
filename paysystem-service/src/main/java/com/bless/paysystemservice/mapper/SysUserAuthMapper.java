package com.bless.paysystemservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bless.paysystemcore.entity.SysUserAuth;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 系统用户认证表 Mapper 接口
 * </p>
 *
 * @author bless
 * @since 2024-07-30
 */
public interface SysUserAuthMapper extends BaseMapper<SysUserAuth> {

    SysUserAuth selectByLogin(@Param("identifier")String identifier,
                              @Param("identityType")Byte identityType,
                              @Param("sysType")String sysType);

}
