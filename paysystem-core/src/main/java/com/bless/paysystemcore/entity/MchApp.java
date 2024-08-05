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
 * 商户应用表
 * </p>
 *
 * @author bless
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mch_app")
@ApiModel(value="MchApp对象", description="商户应用表")
public class MchApp implements Serializable {
    public static final LambdaQueryWrapper<MchApp> gw() {
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应用ID")
    @TableId(value = "app_id", type = IdType.INPUT)
    private String appId;

    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty(value = "商户号")
    private String mchNo;

    @ApiModelProperty(value = "应用状态: 0-停用, 1-正常")
    private Byte state;

    @ApiModelProperty(value = "应用私钥")
    private String appSecret;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createdUid;

    @ApiModelProperty(value = "创建者姓名")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
