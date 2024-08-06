package com.bless.paysystemcomponentsmq.vender;

import com.bless.paysystemcomponentsmq.model.AbstractMQ;

/**
 * @Author bless
 * @Version 1.0
 * @Description MQ 消息发送器 接口定义
 * @Date 2024-08-05 17:05
 */
public interface IMQSender {

    /** 推送MQ消息， 实时 **/
    void send(AbstractMQ mqModel);

    /** 推送MQ消息， 延迟接收，单位：s **/
    void send(AbstractMQ mqModel, int delay);

}
