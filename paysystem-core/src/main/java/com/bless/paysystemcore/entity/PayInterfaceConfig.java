package com.bless.paysystemcore.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bless.paysystemcore.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.parameters.P;

/**
 * <p>
 * 支付接口配置参数表
 * </p>
 *
 * @author bless
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay_interface_config")
@ApiModel(value="PayInterfaceConfig对象", description="支付接口配置参数表")
public class PayInterfaceConfig extends BaseModel implements Serializable {
    public static final LambdaQueryWrapper<PayInterfaceConfig> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "账号类型:1-服务商 2-商户 3-商户应用")
    private Integer infoType;

    @ApiModelProperty(value = "服务商号/商户号/应用ID")
    private String infoId;

    @ApiModelProperty(value = "支付接口代码")
    private String ifCode;

    @ApiModelProperty(value = "接口配置参数,json字符串")
    private String ifParams;

    @ApiModelProperty(value = "支付接口费率")
    private BigDecimal ifRate;

    @ApiModelProperty(value = "状态: 0-停用, 1-启用")
    private Integer state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建者用户ID")
    private Long createdUid;

    @ApiModelProperty(value = "创建者姓名")
    private String createdBy;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新者用户ID")
    private Long updatedUid;

    @ApiModelProperty(value = "更新者姓名")
    private String updatedBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
