package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务商信息表
 * </p>
 *
 * @author bless
 * @since 2024-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("isv_info")
@ApiModel(value="IsvInfo对象", description="服务商信息表")
public class IsvInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务商号")
    @TableId(value = "isv_no", type = IdType.AUTO)
    private String isvNo;

    @ApiModelProperty(value = "服务商名称")
    private String isvName;

    @ApiModelProperty(value = "服务商简称")
    private String isvShortName;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactTel;

    @ApiModelProperty(value = "联系人邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "状态: 0-停用, 1-正常")
    private Integer state;

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
