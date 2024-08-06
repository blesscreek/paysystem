package com.bless.paysystemcomponentsmq.vender.rabbitmq;

import com.bless.paysystemcomponentsmq.constant.MQSendTypeEnum;
import com.bless.paysystemcomponentsmq.constant.MQVenderCS;
import com.bless.paysystemcomponentsmq.model.AbstractMQ;
import com.bless.paysystemcomponentsmq.vender.IMQSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @Author bless
 * @Version 1.0
 * @Description rabbitMQ 消息发送器的实现
 * @Date 2024-08-05 19:24
 */
@Component
//读取application.yml文件中的配置（名为name的内容），比较获取到的属性值与havingValue给定的值是否相同，相同才会加载配置
@ConditionalOnProperty(name = MQVenderCS.YML_VENDER_KEY, havingValue = MQVenderCS.RABBIT_MQ)
public class RabbitMQSender implements IMQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public void send(AbstractMQ mqModel) {
        if (mqModel.getMQType() == MQSendTypeEnum.QUEUE) {
            rabbitTemplate.convertAndSend(mqModel.getMQName(), mqModel.toMessage());
        } else {
            this.rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME_PREFIX + mqModel.getMQName(),
                    null, mqModel.toMessage());
        }

    }

    @Override
    public void send(AbstractMQ mqModel, int delay) {

    }
}
