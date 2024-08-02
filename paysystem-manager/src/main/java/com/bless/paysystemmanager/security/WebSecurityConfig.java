package com.bless.paysystemmanager.security;

import com.bless.paysystemmanager.config.SystemYmlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-07-31 10:43
 */

@Configuration
@EnableWebSecurity//自动配置了一个WebSecurityConfigurerAdapter的bean,可对它扩展
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启@PreAuthorize @PostAuthorize 等前置后置安全校验注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SystemYmlConfig systemYmlConfig;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MyAuthenticationEntryPoint unauthorizedHandler;
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /** 允许跨域请求 **/
    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        if(systemYmlConfig.getAllowCors()){
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowCredentials(true);   //带上cookie信息
//          config.addAllowedOrigin(CorsConfiguration.ALL);  //允许跨域的域名， *表示允许任何域名使用
            config.addAllowedOriginPattern(CorsConfiguration.ALL);  //使用addAllowedOriginPattern 避免出现 When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
            config.addAllowedHeader(CorsConfiguration.ALL);   //允许任何请求头
            config.addAllowedMethod(CorsConfiguration.ALL);   //允许任何方法（post、get等）
            source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        }
        return new CorsFilter(source);
    }

    /**
     * 使用BCrypt强哈希函数 实现PasswordEncoder
     * **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()

                .cors().and()

                // 认证失败处理方式
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 添加JWT filter
        httpSecurity.addFilterBefore(new AuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore文件 ： 无需进入spring security 框架
        // 1.允许对于网站静态资源的无授权访问
        // 2.对于获取token的rest api要允许匿名访问
        web.ignoring().antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/*.svg",
                        "/**/*.ico",
                        "/**/*.webp",
                        "/*.txt",
                        "/**/*.xls",
                        "/**/*.mp4"   //支持mp4格式的文件匿名访问
                )
                .antMatchers(
                        "/api/anon/**", //匿名访问接口
                        "/swagger-resources/**","/v2/api-docs/**" // swagger相关
                );
    }

}
