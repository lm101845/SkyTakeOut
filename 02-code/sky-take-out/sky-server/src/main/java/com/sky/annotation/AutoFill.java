package com.sky.annotation;

/**
 * @Author liming
 * @Date 2023/10/24 8:37
 * @Description
 **/

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解
 */
@Target(ElementType.METHOD)   // 作用于方法
@Retention(RetentionPolicy.RUNTIME)  // 运行时有效
public @interface AutoFill {
    /**
     * 操作类型：UPDATE/INSERT——只在更新或插入时填充
     */
    OperationType value();
}
