package com.file.api.autoconfig;

import com.file.local.autoconfig.config.LocalFileUploadConfiguration;
import com.file.minio.autoconfig.config.MinioAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/19 10:36
 */
public class FileUploadConfigSelecltor implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> attributesMap = annotationMetadata.getAnnotationAttributes(FileUploadSelector.class.getName());
        String[] fileUploadWayStr = (String[]) attributesMap.get("fileUploadWay");
        String[] fileUploadWayClassName = new String[2];

        if (!attributesMap.isEmpty()) {
            for (String fileUploadWay : fileUploadWayStr) {
                if ("minio".equals(fileUploadWay)) {
                    fileUploadWayClassName[0] = MinioAutoConfiguration.class.getName();
                } else if ("local".equals(fileUploadWay)) {
                    fileUploadWayClassName[1] = LocalFileUploadConfiguration.class.getName();
                }
            }
        }
        return fileUploadWayClassName;
    }
}
