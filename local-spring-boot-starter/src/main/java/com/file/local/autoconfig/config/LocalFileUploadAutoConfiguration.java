package com.file.local.autoconfig.config;

import com.file.local.autoconfig.yml.LocalFileUploadYml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Title: 本地文件上传自动装配类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/19 17:21:36
 **/
@EnableConfigurationProperties(value = LocalFileUploadYml.class)
@ConditionalOnProperty(prefix = "local",name = "enabled",havingValue = "true")
public class LocalFileUploadAutoConfiguration {
    private static Logger logger= LogManager.getLogger(LocalFileUploadAutoConfiguration.class.getName());

    {
        logger.info("Initializing localFileUpload");
    }


}