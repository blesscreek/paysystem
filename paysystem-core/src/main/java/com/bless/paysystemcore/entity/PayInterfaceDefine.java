package com.bless.paysystemcore.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bless.paysystemcore.model.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 支付接口定义表
 * </p>
 *
 * @author bless
 * @since 2024-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay_interface_define")
@ApiModel(value="PayInterfaceDefine对象", description="支付接口定义表")
public class PayInterfaceDefine extends BaseModel implements Serializable {
    public static final LambdaQueryWrapper<PayInterfaceDefine> gw() {
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "接口代码 全小写  wxpay alipay ")
    @TableId(value = "if_code", type = IdType.AUTO)
    private String ifCode;

    @ApiModelProperty(value = "接口名称")
    private String ifName;

    @ApiModelProperty(value = "是否支持普通商户模式: 0-不支持, 1-支持")
    private Integer isMchMode;

    @ApiModelProperty(value = "是否支持服务商子商户模式: 0-不支持, 1-支持")
    private Integer isIsvMode;

    @ApiModelProperty(value = "支付参数配置页面类型:1-JSON渲染,2-自定义")
    private Integer configPageType;

    @ApiModelProperty(value = "ISV接口配置定义描述,json字符串")
    private String isvParams;

    @ApiModelProperty(value = "特约商户接口配置定义描述,json字符串")
    private String isvsubMchParams;

    @ApiModelProperty(value = "普通商户接口配置定义描述,json字符串")
    private String normalMchParams;

    @ApiModelProperty(value = "支持的支付方式 ['wxpay_jsapi', 'wxpay_bar']")
    private String wayCodes;

    @ApiModelProperty(value = "页面展示：卡片-图标")
    private String icon;

    @ApiModelProperty(value = "页面展示：卡片-背景色")
    private String bgColor;

    @ApiModelProperty(value = "状态: 0-停用, 1-启用")
    private Integer state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
