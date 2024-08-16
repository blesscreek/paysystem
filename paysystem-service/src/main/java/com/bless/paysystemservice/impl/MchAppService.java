package com.bless.paysystemservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.utils.StringKit;
import com.bless.paysystemservice.mapper.MchAppMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-05 8:57
 */
@Service
public class MchAppService extends ServiceImpl<MchAppMapper, MchApp> {
    public IPage<MchApp> selectPage(IPage iPage, MchApp mchApp) {
        LambdaQueryWrapper<MchApp> wrapper = MchApp.gw();
        if (StringUtils.isNotBlank(mchApp.getMchNo())) {
            wrapper.eq(MchApp::getMchNo, mchApp.getMchNo());
        }
        if (StringUtils.isNotEmpty(mchApp.getAppId())) {
            wrapper.eq(MchApp::getAppId, mchApp.getAppId());
        }
        if (StringUtils.isNotEmpty(mchApp.getAppName())) {
            wrapper.eq(MchApp::getAppName, mchApp.getAppName());
        }
        if (mchApp.getState() != null) {
            wrapper.eq(MchApp::getState, mchApp.getState());
        }
        wrapper.orderByDesc(MchApp::getCreatedAt);

        IPage<MchApp> pages = this.page(iPage, wrapper);

        pages.getRecords().stream().forEach(item -> item.setAppSecret(StringKit.str2Star(item.getAppSecret(), 6, 6, 6)));

        return pages;

    }
    public MchApp getOneByMch(String mchNo, String appId){
        return getOne(MchApp.gw().eq(MchApp::getMchNo, mchNo).eq(MchApp::getAppId, appId));
    }

}
