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

import com.bless.paysystemcore.cache.RedisUtil;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.jwt.JWTPayload;
import com.bless.paysystemcore.jwt.JWTUtils;
import com.bless.paysystemcore.model.security.MyUserDetails;

import com.bless.paysystemcore.utils.SpringBeansUtil;
import com.bless.paysystemmerchant.config.SystemYmlConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p><b>Title: </b>JwtAuthenticationTokenFilter.java
 * <p><b>Description: </b>
 * spring security框架中验证组件的前置过滤器；
 * 用于验证token有效期，并放置ContextAuthentication信息,为后续spring security框架验证提供数据；
 * 避免使用@Component等bean自动装配注解：@Component会将filter被spring实例化为web容器的全局filter，导致重复过滤。
 * @modify terrfly
 * @version V1.0
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 * <p>
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        MyUserDetails myUserDetails = commonFilter(request);

        if(myUserDetails == null){
            chain.doFilter(request, response);
            return;
        }

        //将信息放置到Spring-security context中
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


    private MyUserDetails commonFilter(HttpServletRequest request){


        String authToken = request.getHeader(CS.ACCESS_TOKEN_NAME);
        if(StringUtils.isEmpty(authToken)){
            authToken = request.getParameter(CS.ACCESS_TOKEN_NAME);
        }
        if(StringUtils.isEmpty(authToken)){
            return null; //放行,并交给UsernamePasswordAuthenticationFilter进行验证,返回公共错误信息.
        }

        JWTPayload jwtPayload = JWTUtils.parseToken(authToken, SpringBeansUtil.getBean(SystemYmlConfig.class).getJwtSecret());  //反解析token信息
        //token字符串解析失败
        if( jwtPayload == null || StringUtils.isEmpty(jwtPayload.getCacheKey())) {
            return null;
        }

        //根据用户名查找数据库
        MyUserDetails jwtBaseUser = RedisUtil.getObject(jwtPayload.getCacheKey(), MyUserDetails.class);
        if(jwtBaseUser == null){
            RedisUtil.del(jwtPayload.getCacheKey());
            return null; //数据库查询失败，删除redis
        }

        //续签时间
        RedisUtil.expire(jwtPayload.getCacheKey(), CS.TOKEN_TIME);

        return jwtBaseUser;
    }


}
