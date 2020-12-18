package com.good.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解类，需要申请动态权限
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionNeed {
    //接受申请的权限的数组
    String[] value();
    //申请权限的 requestCode
    int requestCode() default 0;
}
