package com.bless.paysystempayment.util;

import com.bless.paysystempayment.rqrs.AbstractRS;

/**
 * @Author bless
 * @Version 1.0
 * @Description api响应结果构造器
 * @Date 2024-08-12 12:33
 */

public class ApiResBuilder {
    /** 构建自定义响应对象, 默认响应成功 **/
    public static <T extends AbstractRS> T buildSuccess(Class<? extends AbstractRS> T){

        try {
            T result = (T)T.newInstance();
            return result;

        } catch (Exception e) { return null; }
    }


}
