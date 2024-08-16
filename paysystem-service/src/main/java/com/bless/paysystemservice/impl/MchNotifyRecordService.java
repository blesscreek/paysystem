package com.bless.paysystemservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bless.paysystemcore.entity.MchNotifyRecord;
import com.bless.paysystemservice.mapper.MchNotifyRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description 商户通知表 服务实现类
 * @Date 2024-08-12 9:15
 */
@Service
public class MchNotifyRecordService extends ServiceImpl<MchNotifyRecordMapper, MchNotifyRecord> {
    /** 根据订单号和类型查询 */
    public MchNotifyRecord findByOrderAndType(String orderId, Byte orderType){
        return getOne(
                MchNotifyRecord.gw().eq(MchNotifyRecord::getOrderId, orderId).eq(MchNotifyRecord::getOrderType, orderType)
        );
    }

    /** 查询支付订单 */
    public MchNotifyRecord findByPayOrder(String orderId){
        return findByOrderAndType(orderId, MchNotifyRecord.TYPE_PAY_ORDER);
    }


}
