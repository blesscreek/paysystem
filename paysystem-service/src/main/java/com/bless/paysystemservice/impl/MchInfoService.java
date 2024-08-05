package com.bless.paysystemservice.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bless.paysystemcore.constants.ApiCodeEnum;
import com.bless.paysystemcore.constants.CS;
import com.bless.paysystemcore.entity.IsvInfo;
import com.bless.paysystemcore.entity.MchApp;
import com.bless.paysystemcore.entity.MchInfo;
import com.bless.paysystemcore.entity.SysUser;
import com.bless.paysystemcore.exception.BizException;
import com.bless.paysystemservice.mapper.MchInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author bless
 * @Version 1.0
 * @Description
 * @Date 2024-08-03 15:09
 */
@Service
public class MchInfoService extends ServiceImpl<MchInfoMapper, MchInfo> {
    @Autowired
    private IsvInfoService isvInfoService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private MchAppService mchAppService;
    @Transactional(rollbackFor = Exception.class)
    public void addMch(MchInfo mchInfo, String loginUserName) {

        // 校验特邀商户信息
        if (mchInfo.getType() == CS.MCH_TYPE_ISVSUB && StringUtils.isNotEmpty(mchInfo.getIsvNo())) {
            // 当前服务商状态是否正确
            IsvInfo isvInfo = isvInfoService.getById(mchInfo.getIsvNo());
            if (isvInfo == null || isvInfo.getState() == CS.NO) {
                throw new BizException("当前服务商不可用");
            }
        }

        // 插入商户基本信息
        boolean saveResult = save(mchInfo);
        if (!saveResult) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }

        // 插入用户信息
        SysUser sysUser = new SysUser();
        sysUser.setLoginUsername(loginUserName);
        sysUser.setRealname(mchInfo.getContactName());
        sysUser.setTelphone(mchInfo.getContactTel());
        sysUser.setUserNo(mchInfo.getMchNo());
        sysUser.setBelongInfoId(mchInfo.getMchNo());
        sysUser.setSex(CS.SEX_MALE);
        sysUser.setIsAdmin(CS.YES);
        sysUser.setState(mchInfo.getState());
        sysUserService.addSysUser(sysUser, CS.SYS_TYPE.MCH);

        // 插入商户默认应用
        MchApp mchApp = new MchApp();
        mchApp.setAppId(IdUtil.objectId());
        mchApp.setMchNo(mchInfo.getMchNo());
        mchApp.setAppName("默认应用");
        mchApp.setAppSecret(RandomUtil.randomString(128));
        mchApp.setState(CS.YES);
        mchApp.setCreatedBy(sysUser.getRealname());
        mchApp.setCreatedUid(sysUser.getSysUserId());
        saveResult = mchAppService.save(mchApp);
        if (!saveResult) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }

        // 存入商户默认用户ID
        MchInfo updateRecord = new MchInfo();
        updateRecord.setMchNo(mchInfo.getMchNo());
        updateRecord.setInitUserId(sysUser.getSysUserId());
        saveResult = updateById(updateRecord);
        if (!saveResult) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_CREATE);
        }
    }
}
