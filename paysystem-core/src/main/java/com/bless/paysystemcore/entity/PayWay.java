package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bless.paysystemcore.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付方式表
 * </p>
 *
 * @author bless
 * @since 2024-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay_way")
@ApiModel(value="PayWay对象", description="支付方式表")
public class PayWay extends BaseModel {
    public static final LambdaQueryWrapper<PayWay> gw() {
        return new LambdaQueryWrapper();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "支付方式代码  例如： wxpay_jsapi")
    @TableId(value = "way_code", type = IdType.AUTO)
    private String wayCode;

    @ApiModelProperty(value = "支付方式名称")
    private String wayName;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;


}
