package com.echo.common.core.enums;

import cn.hutool.core.util.EnumUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EnumUtils extends EnumUtil {

    /**
     * 根据枚举code获取枚举
     *
     * @param code
     */
    public static <T extends BaseEnum> T getEnumByCode(Class<T> clazz, Byte code) {
        for (T anEnum : clazz.getEnumConstants()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据枚举描述value获取枚举
     *
     * @param value
     */
    public static <T extends BaseEnum> T getEnumByValue(Class<T> clazz, String value) {
        for (T anEnum : clazz.getEnumConstants()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据枚举code获取描述value
     *
     * @param code
     */
    public static <T extends BaseEnum> String getValueByCode(Class<T> clazz, Byte code) {
        for (T anEnum : clazz.getEnumConstants()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum.getValue();
            }
        }
        return null;
    }

    /**
     * 根据枚举描述value获取code
     *
     * @param value
     */
    public static <T extends BaseEnum> Byte getCodeByValue(Class<T> clazz, String value) {
        for (T anEnum : clazz.getEnumConstants()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum.getCode();
            }
        }
        return null;
    }

    /**
     * 根据枚举 某方法(参数) 获取 枚举
     *
     * @param value
     */
    public static <T extends BaseEnum> Byte getEnumByMethod(Class<T> clazz, String value, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            for (T anEnum : clazz.getEnumConstants()) {
                if (method.invoke(anEnum).equals(value)) {
                    return anEnum.getCode();
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
        return null;
    }
}
