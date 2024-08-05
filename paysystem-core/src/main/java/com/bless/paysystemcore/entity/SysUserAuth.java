package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统用户认证表
 * </p>
 *
 * @author bless
 * @since 2024-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_user_auth")
@ApiModel(value="SysUserAuth对象", description="系统用户认证表")
public class SysUserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "auth_id", type = IdType.AUTO)
    private Long authId;

    @ApiModelProperty(value = "user_id")
    private Long userId;

    @ApiModelProperty(value = "登录类型  1-登录账号 2-手机号 3-邮箱  10-微信  11-QQ 12-支付宝 13-微博")
    private Byte identityType;

    @ApiModelProperty(value = "认证标识 ( 用户名 | open_id )")
    private String identifier;

    @ApiModelProperty(value = "密码凭证")
    private String credential;

    @ApiModelProperty(value = "salt")
    private String salt;

    @ApiModelProperty(value = "所属系统： MGR-运营平台, MCH-商户中心")
    private String sysType;


}
