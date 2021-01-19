package com.minio.autoconfig.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Title: Minio yml配置文件类
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/11 11:09
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioYml {
    //URL
    private String endpoint;
    //端口
    private Integer port;
    //账号
    private String accessKey;
    //密码
    private String secretKey;
    //如果是true，则用的是https而不是http,默认值是true
    private Boolean secure;
    //默认存储桶
    private String bucketName;
    //配置目录
    private String configDir;

}
