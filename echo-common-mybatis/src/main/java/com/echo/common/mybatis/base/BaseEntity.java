package com.echo.common.mybatis.base;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象实体
 *
 * @author echo
 * @date 2021/8/9
 */
@Getter
@Setter
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id，不赋予业务意义
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "自增id，不赋予业务意义")
    private Long id;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 是否逻辑删除：0-已删除，1-未删除
     */
    @Schema(description = "是否逻辑删除：0-已删除，1-未删除")
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Byte delFlag;

    /**
     * 请求参数 --- 可以用来装不是很必要做成字段的参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
