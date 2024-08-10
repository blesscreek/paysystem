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
package com.bless.paysystemmerchant.secruity;


import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.entity.SysUserAuth;
import com.bless.paysystemcore.exception.MyAuthenticationException;
import com.bless.paysystemcore.model.security.MyUserDetails;
import com.bless.paysystemcore.utils.RegKit;
import com.bless.paysystemservice.impl.SysUserAuthService;
import com.bless.paysystemservice.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserDetailsService实现类
 *
 * @author terrfly
 * @modify zhuxiao
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserAuthService sysUserAuthService;

    /**
     *
     * 此函数为： authenticationManager.authenticate(upToken) 内部调用 ;
     * 需返回 用户信息载体 / 用户密码  。
     * 用户角色+权限的封装集合 (暂时不查询， 在验证通过后再次查询，避免用户名密码输入有误导致查询资源浪费)
     *
     * **/
    @Override
    public UserDetails loadUserByUsername(String loginUsernameStr) throws UsernameNotFoundException {

        //登录方式， 默认为账号密码登录
        Byte identityType = CS.AUTH_TYPE.LOGIN_USER_NAME;
        if(RegKit.isMobile(loginUsernameStr)){
            identityType = CS.AUTH_TYPE.TELPHONE; //手机号登录
        }

        //首先根据登录类型 + 用户名得到 信息
        SysUserAuth auth = sysUserAuthService.selectByLogin(loginUsernameStr, identityType, CS.SYS_TYPE.MCH);

        if(auth == null){ //没有该用户信息
            throw MyAuthenticationException.build("用户名/密码错误！");
        }

        //用户ID
        Long userId = auth.getUserId();

        SysUser sysUser = sysUserService.getById(userId);

        if (sysUser == null) {
            throw MyAuthenticationException.build("用户名/密码错误！");
        }

        if(CS.PUB_USABLE != sysUser.getState()){ //状态不合法
            throw MyAuthenticationException.build("用户状态不可登录，请联系管理员！");
        }

        return new MyUserDetails(sysUser, auth.getCredential());

    }
}
