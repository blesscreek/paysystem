package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商户信息表
 * </p>
 *
 * @author bless
 * @since 2024-08-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mch_info")
@ApiModel(value="MchInfo对象", description="商户信息表")
public class MchInfo implements Serializable {
    public static final LambdaQueryWrapper<MchInfo> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;
    public static final byte TYPE_NORMAL = 1; //商户类型： 1-普通商户
    public static final byte TYPE_ISVSUB = 2; //商户类型： 2-特约商户

    @ApiModelProperty(value = "商户号")
    @TableId(value = "mch_no", type = IdType.INPUT)
    private String mchNo;

    @ApiModelProperty(value = "商户名称")
    private String mchName;

    @ApiModelProperty(value = "商户简称")
    private String mchShortName;

    @ApiModelProperty(value = "类型: 1-普通商户, 2-特约商户(服务商模式)")
    private Byte type;

    @ApiModelProperty(value = "服务商号")
    private String isvNo;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactTel;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "商户状态: 0-停用, 1-正常")
    private Byte state;

    @ApiModelProperty(value = "商户备注")
    private String remark;

    @ApiModelProperty(value = "初始用户ID（创建商户时，允许商户登录的用户）")
    private Long initUserId;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createdUid;

    @ApiModelProperty(value = "创建者姓名")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;


}
