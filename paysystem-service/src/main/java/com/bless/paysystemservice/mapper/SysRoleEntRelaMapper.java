package com.bless.paysystemservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bless.paysystemcore.entity.SysRoleEntRela;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-02 9:58
 */

public interface SysRoleEntRelaMapper extends BaseMapper<SysRoleEntRela> {
    List<String> selectEntIdsByUserId(@Param("userId") Long userId, @Param("sysType") String sysType);
}
