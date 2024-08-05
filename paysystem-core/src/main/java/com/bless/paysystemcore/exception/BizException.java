package com.bless.paysystemcore.exception;

import com.bless.paysystemcore.constants.ApiCodeEnum;
import com.bless.paysystemcore.model.ApiRes;
import lombok.Getter;

/**
 * @Author bless
 * @Version 1.0
 * @Description 自定义业务异常
 * @Date 2024-07-30 21:53
 */
@Getter
public class BizException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private ApiRes apiRes;
    /** 业务自定义异常 **/
    public BizException(String msg) {
        super(msg);
        this.apiRes = ApiRes.customFail(msg);
    }
    public BizException(ApiCodeEnum apiCodeEnum, String... params) {
        super();
        apiRes = ApiRes.fail(apiCodeEnum, params);
    }

    public BizException(ApiRes apiRes) {
        super(apiRes.getMsg());
        this.apiRes = apiRes;
    }
}

