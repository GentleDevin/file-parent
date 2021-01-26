package com.file.minio.autoconfig.config;

import com.file.minio.autoconfig.yml.MinioYml;
import com.file.minio.utils.MinioUtil;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Title: Minio 自动装配类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/07 11:26:36
 **/
@ConditionalOnClass(MinioClient.class)
@ConditionalOnProperty(prefix = "minio",name = "enabled",havingValue = "true")
@EnableConfigurationProperties(value = MinioYml.class)
public class MinioAutoConfiguration {
    private static Logger logger= LogManager.getLogger(MinioAutoConfiguration.class.getName());

    @Bean
    public MinioClient getMinioClient(MinioYml minioYml) throws InvalidPortException, InvalidEndpointException {
        logger.info("Initializing MinioClient");
        MinioClient minioClient = new MinioClient(minioYml.getEndpoint(),minioYml.getPort(),minioYml.getAccessKey(), minioYml.getSecretKey(),minioYml.getSecure());
        return minioClient;
    }

    @Bean
    public MinioUtil getMinioUtil() {
        logger.info("Initializing MinioUtil");
        return new MinioUtil();
    }

}