package com.bless.paysystemcore.entity;

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
 * 商户通知记录表
 * </p>
 *
 * @author bless
 * @since 2024-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mch_notify_record")
@ApiModel(value="MchNotifyRecord对象", description="商户通知记录表")
public class MchNotifyRecord implements Serializable {
    //订单类型:1-支付,2-退款, 3-转账
    public static final byte TYPE_PAY_ORDER = 1;
    public static final byte TYPE_REFUND_ORDER = 2;
    public static final byte TYPE_TRANSFER_ORDER = 3;

    //通知状态
    public static final byte STATE_ING = 1;
    public static final byte STATE_SUCCESS = 2;
    public static final byte STATE_FAIL = 3;

    //gw
    public static final LambdaQueryWrapper<MchNotifyRecord> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商户通知记录ID")
    @TableId(value = "notify_id", type = IdType.AUTO)
    private Long notifyId;

    @ApiModelProperty(value = "订单ID")
    private String orderId;

    @ApiModelProperty(value = "订单类型:1-支付,2-退款")
    private Byte orderType;

    @ApiModelProperty(value = "商户订单号")
    private String mchOrderNo;

    @ApiModelProperty(value = "商户号")
    private String mchNo;

    @ApiModelProperty(value = "服务商号")
    private String isvNo;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "通知响应结果")
    private String resResult;

    @ApiModelProperty(value = "通知次数")
    private Integer notifyCount;

    @ApiModelProperty(value = "最大通知次数, 默认6次")
    private Integer notifyCountLimit;

    @ApiModelProperty(value = "通知状态,1-通知中,2-通知成功,3-通知失败")
    private Byte state;

    @ApiModelProperty(value = "最后一次通知时间")
    private LocalDateTime lastNotifyTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
