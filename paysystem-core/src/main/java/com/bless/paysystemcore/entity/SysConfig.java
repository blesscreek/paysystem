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
 * 系统配置表
 * </p>
 *
 * @author bless
 * @since 2024-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_config")
@ApiModel(value="SysConfig对象", description="系统配置表")
public class SysConfig implements Serializable {

    //gw
    public static final LambdaQueryWrapper<SysConfig> gw(){
        return new LambdaQueryWrapper<>();
    }


    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置KEY")
    @TableId(value = "config_key", type = IdType.AUTO)
    private String configKey;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "描述信息")
    private String configDesc;

    @ApiModelProperty(value = "分组key")
    private String groupKey;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "配置内容项")
    private String configVal;

    @ApiModelProperty(value = "类型: text-输入框, textarea-多行文本, uploadImg-上传图片, switch-开关")
    private String type;

    @ApiModelProperty(value = "显示顺序")
    private Long sortNum;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;


}
