/*
 * Copyright (c) 2021-2031, 河北计全科技有限公司 (https://www.jeequan.com & jeequan@126.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bless.paysystemmerchant.ctrl;


import com.bless.paysystemcore.constants.ApiCodeEnum;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.ctrls.AbstractCtrl;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.model.ApiRes;
import com.bless.paysystemcore.model.security.MyUserDetails;
import com.bless.paysystemmerchant.config.SystemYmlConfig;
import com.bless.paysystemservice.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 通用ctrl类
 *
 * @author terrfly
 * @modify zhuxiao
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
public abstract class CommonCtrl extends AbstractCtrl {

    @Autowired
    protected SystemYmlConfig mainConfig;

    @Autowired
    private SysConfigService sysConfigService;

    /** 获取当前用户ID */
    protected MyUserDetails getCurrentUser(){

        return (MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /** 获取当前商户ID **/
    protected String getCurrentMchNo() {
        return getCurrentUser().getSysUser().getBelongInfoId();
    }

    /**
     * 获取当前用户登录IP
     * @return
     */
    protected String getIp() {
        return getClientIp();
    }

    /**
     * 校验当前用户是否为超管
     * @return
     */
    protected ApiRes checkIsAdmin() {
        SysUser sysUser = getCurrentUser().getSysUser();
        if (sysUser.getIsAdmin() != CS.YES) {
            return ApiRes.fail(ApiCodeEnum.SYS_PERMISSION_ERROR);
        }else {
            return null;
        }

    }

}
