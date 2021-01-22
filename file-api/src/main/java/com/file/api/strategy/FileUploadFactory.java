package com.file.api.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 10:59
 */
public class FileUploadFactory {
    private static Logger logger= LogManager.getLogger(FileUploadFactory.class.getName());
    private static Map<String, IFileUpload> UPLOAD_MAP = new HashMap<>();
    public static final String LOCAL ="LOCAL";
    public static final String MIN_IO  ="MIN_IO";

    static {
        UPLOAD_MAP.put(FileUploadFactory.LOCAL, new LocalFileUpload());
        UPLOAD_MAP.put(FileUploadFactory.MIN_IO, new MinioFileUpload());
        logger.info("Initializing fileUploadFactory UPLOAD_MAP");
    }

    public static IFileUpload getFileUploadByKey(String fileUploadKey) {
        IFileUpload fileUpload = UPLOAD_MAP.get(fileUploadKey);
        return fileUpload == null ? null : fileUpload;
    }


    public static Set<String> FileUploadKeys() {
        return UPLOAD_MAP.keySet();
    }
}
