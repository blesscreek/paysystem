package com.bless.paysystemmanager.security;

import com.bless.paysystemcore.cache.RedisUtil;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.jwt.JWTPayload;
import com.bless.paysystemcore.jwt.JWTUtils;
import com.bless.paysystemcore.model.security.MyUserDetails;
import com.bless.paysystemcore.utils.SpringBeansUtil;
import com.bless.paysystemmanager.config.SystemYmlConfig;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author bless
 * @Version 1.0
 * @Description spring security框架中验证组件的前置过滤器；
 *  * 用于验证token有效期，并放置ContextAuthentication信息,为后续spring security框架验证提供数据；
 *  * 避免使用@Component等bean自动装配注解：@Component会将filter被spring实例化为web容器的全局filter，导致重复过滤。
 * @Date 2024-07-31 11:27
 */

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        MyUserDetails myUserDetails = commonFilter(request);
        if (myUserDetails == null) {
            chain.doFilter(request, response);
            return;
        }
        //将信息放置在Spring-security context中
        UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(myUserDetails, null, myUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
    private MyUserDetails commonFilter(HttpServletRequest request) {
        String authToken = request.getHeader(CS.ACCESS_TOKEN_NAME);
        if (StringUtils.isEmpty(authToken)) {
            authToken = request.getParameter(CS.ACCESS_TOKEN_NAME);
        }
        if (StringUtils.isEmpty(authToken)) {
            return null;//放行,并交给UsernamePasswordAuthenticationFilter进行验证,返回公共错误信息.
        }
        JWTPayload jwtPayload = JWTUtils.parseToken(authToken, SpringBeansUtil.getBean(SystemYmlConfig.class).getJwtSecret());
        //token字符串解析失败
        if (jwtPayload == null || StringUtils.isEmpty(jwtPayload.getCacheKey())) {
            return null;
        }
        //根据用户名查找数据库
        MyUserDetails jwtBaseUser = RedisUtil.getObject(jwtPayload.getCacheKey(), MyUserDetails.class);
        if (jwtBaseUser == null) {
            RedisUtil.del(jwtPayload.getCacheKey());
            return null;//数据库查询失败，删除redis
        }

        RedisUtil.expire(jwtPayload.getCacheKey(),  CS.TOKEN_TIME);
        return jwtBaseUser;
    }
}
