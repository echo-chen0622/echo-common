package com.echo.common.web.utils;

import cn.hutool.core.text.CharSequenceUtil;
import com.echo.common.web.exception.ApiException;

/**
 * sql操作工具类
 *
 * @author echo
 */
public class SqlUtil {
    /**
     * 定义常用的 sql关键字
     */
    public static String SQL_REGEX = "and |extractvalue|updatexml|exec |insert |select |delete |update |drop |count |chr |mid |master |truncate |char |declare |or |+|user()";

    /**
     * 仅支持字母、数字、下划线、空格、逗号、小数点（支持多个字段排序）
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * 检查字符，防止注入绕过
     */
    public static String escapeOrderBySql(String value) {
        if (value != null && !isValidOrderBySql(value)) {
            throw new ApiException("参数不符合规范，不能进行查询");
        }
        return value;
    }

    /**
     * 验证 order by 语法是否符合规范
     */
    public static boolean isValidOrderBySql(String value) {
        return value.matches(SQL_PATTERN);
    }

    /**
     * SQL关键字检查
     */
    public static void filterKeyword(String value) {
        if (value == null) {
            return;
        }
        String[] sqlKeywords = CharSequenceUtil.splitToArray(SQL_REGEX, "\\|");
        for (String sqlKeyword : sqlKeywords) {
            if (CharSequenceUtil.indexOfIgnoreCase(value, sqlKeyword) > -1) {
                throw new ApiException("参数存在SQL注入风险");
            }
        }
    }
}
