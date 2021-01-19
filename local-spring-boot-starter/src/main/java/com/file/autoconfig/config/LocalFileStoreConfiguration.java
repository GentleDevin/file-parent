package com.file.autoconfig.config;

import com.file.autoconfig.yml.LocalFileStore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Title: 本地文件存储自动装配类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/19 17:21:36
 **/
@EnableConfigurationProperties(value = LocalFileStore.class)
public class LocalFileStoreConfiguration {
    private static Logger logger= LogManager.getLogger(LocalFileStoreConfiguration.class.getName());

    @Bean
    public LocalFileStore getLocalFileStore() {
        logger.info("Initializing localFileStore");
        return new LocalFileStore();
    }

}