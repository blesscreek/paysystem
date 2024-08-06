package com.bless.paysystemcomponentsmq.model;

import com.bless.paysystemcomponentsmq.constant.MQSendTypeEnum;

/**
 * @Author bless
 * @Version 1.0
 * @Description 定义MQ消息格式
 * @Date 2024-08-05 16:46
 */

public abstract class AbstractMQ {
    /** MQ名称 **/
    public abstract String getMQName();

    /** MQ 类型 **/
    public abstract MQSendTypeEnum getMQType();

    /** 构造MQ消息体 String类型 **/
    public abstract String toMessage();

}
