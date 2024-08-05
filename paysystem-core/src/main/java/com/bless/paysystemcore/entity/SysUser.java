package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bless.paysystemcore.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author bless
 * @since 2024-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user")
@ApiModel(value="SysUser对象", description="系统用户表")
public class SysUser extends BaseModel {
    public static final LambdaQueryWrapper<SysUser> gw() {
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "系统用户ID")
    @TableId(value = "sys_user_id", type = IdType.AUTO)
    private Long sysUserId;

    @ApiModelProperty(value = "登录用户名")
    private String loginUsername;

    @ApiModelProperty(value = "真实姓名")
    private String realname;

    @ApiModelProperty(value = "手机号")
    private String telphone;

    @ApiModelProperty(value = "性别 0-未知, 1-男, 2-女")
    private Byte sex;

    @ApiModelProperty(value = "头像地址")
    private String avatarUrl;

    @ApiModelProperty(value = "员工编号")
    private String userNo;

    @ApiModelProperty(value = "是否超管（超管拥有全部权限） 0-否 1-是")
    private Byte isAdmin;

    @ApiModelProperty(value = "状态 0-停用 1-启用")
    private Byte state;

    @ApiModelProperty(value = "所属系统： MGR-运营平台, MCH-商户中心")
    private String sysType;

    @ApiModelProperty(value = "所属商户ID / 0(平台)")
    private String belongInfoId;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;


}
