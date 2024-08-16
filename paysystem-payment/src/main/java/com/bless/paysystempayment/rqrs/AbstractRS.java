package com.bless.paysystempayment.rqrs;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author bless
 * @Version 1.0
 * @Description 接口抽象RS对象, 本身无需实例化
 * @Date 2024-08-10 17:00
 */
@Data
public abstract class AbstractRS implements Serializable {

    public String toJSONString(){
        return JSON.toJSONString(this);
    }

}
