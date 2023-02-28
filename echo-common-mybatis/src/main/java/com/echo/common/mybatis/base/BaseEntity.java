package com.echo.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽象实体
 *
 * @author echo
 * @date 2021/8/9
 */
@Getter
@Setter
public class BaseEntity implements Serializable {
    /**
     * 自增id，不赋予业务意义
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "自增id，不赋予业务意义")
    private Integer id;

    /**
     * 创建者
     */
    @Schema(description = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @Schema(description = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 是否逻辑删除：0-已删除，1-未删除
     */
    @Schema(description = "是否逻辑删除：0-已删除，1-未删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Byte delFlag;

}
