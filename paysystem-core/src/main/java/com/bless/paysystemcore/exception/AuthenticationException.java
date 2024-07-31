package com.bless.paysystemcore.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-07-30 21:44
 */
@Getter
@Setter
public class AuthenticationException extends InternalAuthenticationServiceException {
    private BizException bizException;
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public static AuthenticationException build(String msg){
        return build(new BizException(msg));
    }

    public static AuthenticationException build(BizException ex){

        AuthenticationException result = new AuthenticationException(ex.getMessage());
        result.setBizException(ex);
        return result;
    }

}
