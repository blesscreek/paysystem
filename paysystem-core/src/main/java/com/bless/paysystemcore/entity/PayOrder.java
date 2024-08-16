package com.bless.paysystemcore.entity;

import java.math.BigDecimal;
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
 * 支付订单表
 * </p>
 *
 * @author bless
 * @since 2024-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pay_order")
@ApiModel(value="PayOrder对象", description="支付订单表")
public class PayOrder implements Serializable {
    public static final LambdaQueryWrapper<PayOrder> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;


    public static final byte STATE_INIT = 0; //订单生成
    public static final byte STATE_ING = 1; //支付中
    public static final byte STATE_SUCCESS = 2; //支付成功
    public static final byte STATE_FAIL = 3; //支付失败
    public static final byte STATE_CANCEL = 4; //已撤销
    public static final byte STATE_REFUND = 5; //已退款
    public static final byte STATE_CLOSED = 6; //订单关闭

    public static final byte REFUND_STATE_NONE = 0; //未发生实际退款
    public static final byte REFUND_STATE_SUB = 1; //部分退款
    public static final byte REFUND_STATE_ALL = 2; //全额退款


    public static final byte DIVISION_MODE_FORBID = 0; //该笔订单不允许分账
    public static final byte DIVISION_MODE_AUTO = 1; //支付成功按配置自动完成分账
    public static final byte DIVISION_MODE_MANUAL = 2; //商户手动分账(解冻商户金额)

    public static final byte DIVISION_STATE_UNHAPPEN = 0; //未发生分账
    public static final byte DIVISION_STATE_WAIT_TASK = 1; //等待分账任务处理
    public static final byte DIVISION_STATE_ING = 2; //分账处理中
    public static final byte DIVISION_STATE_FINISH = 3; //分账任务已结束(不体现状态)


    @ApiModelProperty(value = "支付订单号")
    @TableId
    private String payOrderId;

    @ApiModelProperty(value = "商户号")
    private String mchNo;

    @ApiModelProperty(value = "服务商号")
    private String isvNo;

    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "商户名称")
    private String mchName;

    @ApiModelProperty(value = "类型: 1-普通商户, 2-特约商户(服务商模式)")
    private Byte mchType;

    @ApiModelProperty(value = "商户订单号")
    private String mchOrderNo;

    @ApiModelProperty(value = "支付接口代码")
    private String ifCode;

    @ApiModelProperty(value = "支付方式代码")
    private String wayCode;

    @ApiModelProperty(value = "支付金额,单位分")
    private Long amount;

    @ApiModelProperty(value = "商户手续费费率快照")
    private BigDecimal mchFeeRate;

    @ApiModelProperty(value = "商户手续费,单位分")
    private Long mchFeeAmount;

    @ApiModelProperty(value = "三位货币代码,人民币:cny")
    private String currency;

    @ApiModelProperty(value = "支付状态: 0-订单生成, 1-支付中, 2-支付成功, 3-支付失败, 4-已撤销, 5-已退款, 6-订单关闭")
    private Byte state;

    @ApiModelProperty(value = "向下游回调状态, 0-未发送,  1-已发送")
    private Integer notifyState;

    @ApiModelProperty(value = "客户端IP")
    private String clientIp;

    @ApiModelProperty(value = "商品标题")
    private String subject;

    @ApiModelProperty(value = "商品描述信息")
    private String body;

    @ApiModelProperty(value = "特定渠道发起额外参数")
    private String channelExtra;

    @ApiModelProperty(value = "渠道用户标识,如微信openId,支付宝账号")
    private String channelUser;

    @ApiModelProperty(value = "渠道订单号")
    private String channelOrderNo;

    @ApiModelProperty(value = "退款状态: 0-未发生实际退款, 1-部分退款, 2-全额退款")
    private Integer refundState;

    @ApiModelProperty(value = "退款次数")
    private Integer refundTimes;

    @ApiModelProperty(value = "退款总金额,单位分")
    private Long refundAmount;

    @ApiModelProperty(value = "订单分账模式：0-该笔订单不允许分账, 1-支付成功按配置自动完成分账, 2-商户手动分账(解冻商户金额)")
    private Byte divisionMode;

    @ApiModelProperty(value = "订单分账状态：0-未发生分账, 1-等待分账任务处理, 2-分账处理中, 3-分账任务已结束(不体现状态)")
    private Integer divisionState;

    @ApiModelProperty(value = "最新分账时间")
    private LocalDateTime divisionLastTime;

    @ApiModelProperty(value = "渠道支付错误码")
    private String errCode;

    @ApiModelProperty(value = "渠道支付错误描述")
    private String errMsg;

    @ApiModelProperty(value = "商户扩展参数")
    private String extParam;

    @ApiModelProperty(value = "异步通知地址")
    private String notifyUrl;

    @ApiModelProperty(value = "页面跳转地址")
    private String returnUrl;

    @ApiModelProperty(value = "订单失效时间")
    private Date expiredTime;

    @ApiModelProperty(value = "订单支付成功时间")
    private Date successTime;

    @ApiModelProperty(value = "创建时间")
    private Date createdAt;

    @ApiModelProperty(value = "更新时间")
    private Date updatedAt;


}
