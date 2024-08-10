package com.bless.paysystemcore.service;

import com.bless.paysystemcore.model.DBApplicationConfig;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-07-29 19:41
 */
public interface ISysConfigService {
    /** 获取应用的配置参数 **/
    DBApplicationConfig getDBApplicationConfig();


}
