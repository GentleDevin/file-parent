package com.file.local.autoconfig.yml;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Title: 本地文件上传yml配置文件类
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/11 11:09
 */
@Data
@ConfigurationProperties(prefix = "local")
public class LocalFileUploadYml {

    /**
     * 上传文件路径
     **/
    private String uploadPath;

    /**
     * 访问映射文件路径
     **/
    private String accessPath;

    /**
     * 是否启用，true启用，false不启用
     **/
    private boolean enabled;

}
