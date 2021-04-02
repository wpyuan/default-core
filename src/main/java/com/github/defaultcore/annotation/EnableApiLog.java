package com.github.defaultcore.annotation;


import com.github.defaultcore.aop.handler.IApiLogDataHandler;

import java.lang.annotation.*;

/**
 * @author PeiYuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface EnableApiLog {
    /**
     * 接口代码
     */
    String code() default "接口代码";
    /**
     * 接口描述
     */
    String desc() default "接口描述";

    Class<? extends IApiLogDataHandler> handler();
}
