package com.bless.paysystemmanager.ctrl;

import com.bless.paysystemcore.ctrls.AbstractCtrl;
import com.bless.paysystemcore.model.security.MyUserDetails;
import com.bless.paysystemmanager.config.SystemYmlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author bless
 * @Version 1.0
 * @Description 定义通用CommonCtrl
 * @Date 2024-07-31 16:38
 */

public class CommonCtrl extends AbstractCtrl {
    @Autowired
    protected SystemYmlConfig mainConfig;
    /** 获取当前用户ID */
    protected MyUserDetails getCurrentUser() {
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前用户登录IP
     * @return
     */
    protected String getIp() {
        return getClientIp();
    }


}
