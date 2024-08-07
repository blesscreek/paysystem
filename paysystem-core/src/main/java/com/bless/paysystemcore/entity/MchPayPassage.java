package com.bless.paysystemcore.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商户支付通道表
 * </p>
 *
 * @author bless
 * @since 2024-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mch_pay_passage")
@ApiModel(value="MchPayPassage对象", description="商户支付通道表")
public class MchPayPassage implements Serializable {
    public static final LambdaQueryWrapper<MchPayPassage> gw() {
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商户号")
    private String mchNo;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "支付接口")
    private String ifCode;

    @ApiModelProperty(value = "支付方式")
    private String wayCode;

    @ApiModelProperty(value = "支付方式费率")
    private BigDecimal rate;

    @ApiModelProperty(value = "风控数据")
    private String riskConfig;

    @ApiModelProperty(value = "状态: 0-停用, 1-启用")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
