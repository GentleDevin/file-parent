package com.file.autoconfig;

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
@Import({FileStoreConfigSelecltor.class})  //自定义的配置类
public @interface FileStoreSelector {
    String fileStoreWay() default "local";

}
