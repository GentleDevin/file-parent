package com.file.api.autoconfig;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/19 10:04
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FileUploadConfigSelecltor.class})  //自定义的配置类
public @interface FileUploadSelector {
    String[]  fileUploadWay() default "local";
}
