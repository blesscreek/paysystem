package com.bless.paysystemservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bless.paysystemcore.entity.SysConfig;
import com.bless.paysystemcore.service.ISysConfigService;
import com.bless.paysystemservice.mapper.SysConfigMapper;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-07-29 20:03
 */
@Service
public class SysConfigService extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {
    /**
     * 是否启用缓存
     * true:表示将使用内存缓存，将部分系统配置项 或 商户应用/服务商信息进行缓存并读取
     * false:直接查询DB
     */
    public static Boolean IS_USE_CACHE = false;

}
