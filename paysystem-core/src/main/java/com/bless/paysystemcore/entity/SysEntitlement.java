package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统权限表
 * </p>
 *
 * @author bless
 * @since 2024-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_entitlement")
@ApiModel(value="SysEntitlement对象", description="系统权限表")
public class SysEntitlement implements Serializable {

    //gw
    public static final LambdaQueryWrapper<SysEntitlement> gw(){
        return new LambdaQueryWrapper<>();
    }


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "权限ID[ENT_功能模块_子模块_操作], eg: ENT_ROLE_LIST_ADD")
    @TableId(value = "ent_id", type = IdType.AUTO)
    private String entId;

    @ApiModelProperty(value = "权限名称")
    private String entName;

    @ApiModelProperty(value = "菜单图标")
    private String menuIcon;

    @ApiModelProperty(value = "菜单uri/路由地址")
    private String menuUri;

    @ApiModelProperty(value = "组件Name（前后端分离使用）")
    private String componentName;

    @ApiModelProperty(value = "权限类型 ML-左侧显示菜单, MO-其他菜单, PB-页面/按钮")
    private String entType;

    @ApiModelProperty(value = "快速开始菜单 0-否, 1-是")
    private Integer quickJump;

    @ApiModelProperty(value = "状态 0-停用, 1-启用")
    private Integer state;

    @ApiModelProperty(value = "父ID")
    private String pid;

    @ApiModelProperty(value = "排序字段, 规则：正序")
    private Integer entSort;

    @ApiModelProperty(value = "所属系统： MGR-运营平台, MCH-商户中心")
    private String sysType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
