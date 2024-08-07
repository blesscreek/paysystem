package com.bless.paysystemservice.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bless.paysystemcore.entity.MchPayPassage;

import java.util.List;
import java.util.Map;

/**
 * @Author bless
 * @Version 1.0
 * @Description TODO
 * @Date 2024-08-06 21:21
 */
public interface MchPayPassageMapper extends BaseMapper<MchPayPassage> {
    /** 根据支付方式查询可用的支付接口列表 **/
    List<JSONObject> selectAvailablePayInterfaceList(Map params);
}
