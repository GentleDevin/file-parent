package com.file.autoconfig.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Title: Minio yml配置文件类
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/11 11:09
 */
@Data
@ConfigurationProperties(prefix = "local")
public class LocalFileStore {

    //配置目录
    private String configDir;

}
