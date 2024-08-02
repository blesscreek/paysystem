package com.bless.paysystemcore.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @Author bless
 * @Version 1.0
 * @Description  Spring Security 框架自定义异常类
 * @Date 2024-07-30 21:44
 */
@Getter
@Setter
public class MyAuthenticationException extends InternalAuthenticationServiceException {
    private BizException bizException;
    public MyAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyAuthenticationException(String message) {
        super(message);
    }

    public static MyAuthenticationException build(String msg){
        return build(new BizException(msg));
    }

    public static MyAuthenticationException build(BizException ex){

        MyAuthenticationException result = new MyAuthenticationException(ex.getMessage());
        result.setBizException(ex);
        return result;
    }

}
