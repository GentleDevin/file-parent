package com.file.local.autoconfig.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @Title:  上传文件路径映射配置类
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 16:58
 */
@Configuration
public class LocalFileUploadPathConfig extends WebMvcConfigurationSupport {

    @Value("${local.uploadPath}")
    private String uploadPath;

    @Value("${local.accessPath}")
    private String accessPath;

    @Override
    protected void configureMessageConverters(List converters) {
        converters.add(responseBodyConverter());
    }
    @Bean
    public HttpMessageConverter responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(System.getProperty("os.name").toLowerCase().contains("linux")){
            registry.addResourceHandler(accessPath+"/**").addResourceLocations("file:"+uploadPath);
        }else if(System.getProperty("os.name").toLowerCase().contains("windows")){
            registry.addResourceHandler(accessPath+"/**").addResourceLocations("file:"+uploadPath);
        }
    }


}
