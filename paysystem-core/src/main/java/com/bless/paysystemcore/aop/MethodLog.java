package com.bless.paysystemcore.aop;

import java.lang.annotation.*;

/**
 * 方法级日志切面注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})//注解可以应用的目标 可以应用于方法 可以应用于类、接口（包括注解类型）或枚举
@Retention(RetentionPolicy.RUNTIME) //注解的保留策略 注解在运行时保留，可以通过反射获取。这意味着在应用程序运行时，可以通过反射机制读取 MethodLog 注解的信息。
@Documented //注解的方法或类在生成 Javadoc 文档时，注解的信息也会被包括进去。
public @interface MethodLog {
    String remark() default "";
}
