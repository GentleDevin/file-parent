package com.file.autoconfig;

import com.minio.autoconfig.config.MinioAutoConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.nio.file.FileStore;
import java.util.Map;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/19 10:36
 */
public class FileStoreConfigSelecltor implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> attributesMap = annotationMetadata.getAnnotationAttributes(FileStoreSelector.class.getName());
        Object fileStoreWay = attributesMap.get("fileStoreWay");

        if ("minio".equals(fileStoreWay)) {
            return new String[]{MinioAutoConfiguration.class.getName()};
        }else{
            return new String[]{FileStore.class.getName()};
        }
    }

}
