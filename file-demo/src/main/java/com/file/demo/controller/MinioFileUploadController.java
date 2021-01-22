package com.file.demo.controller;


import com.file.api.strategy.FileUploadFactory;
import com.file.api.strategy.IFileUpload;
import com.file.commons.common.ResponseResult;
import com.file.commons.enums.FileState;
import com.file.commons.utils.io.SpringMvcFileUtils;
import com.file.demo.entity.FileInfo;
import com.file.demo.service.MinioFileUploadService;
import com.file.minio.autoconfig.yml.MinioYml;
import com.file.minio.utils.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: Minio文件信息控制类
 * @Description: 提供文件上传、下载、删除
 * @Author: Devin
 * @CreateDate: 2021/01/12 17:27:10
 **/
@RestController
@RequestMapping("/minio")
public class MinioFileUploadController {
    private static Logger logger= LogManager.getLogger(MinioFileUploadController.class.getName());

    @Resource
    private MinioFileUploadService minioFileUploadService;
    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private MinioYml minioYml;

    private IFileUpload fileUpload = FileUploadFactory.getFileUploadByKey(FileUploadFactory.MIN_IO);


    /**
     * @Description: 文件上传
     * @CreateDate: 2021/01/11 10:03:45
     * @param files: 多个file文件
     * @return: java.lang.String
     **/
    @PostMapping("/upload")
    public String uploadFile(MultipartFile[] files, HttpServletRequest request) {
        List<ResponseResult> responseResult = null;
        try {
            responseResult = fileUpload.uploadFile(files,minioYml.getBucketName(),minioYml.getUploadPath());
           minioFileUploadService.save(responseResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return FileState.FILE_UPLOAD_ERROR.getValue();
        }
        return responseResult.toString();
    }


    /**
     * @Description: 文件下载
     * @CreateDate: 2021/01/12 17:26:56
     * @param response: 响应对象
     * @param filePath: 文件路径
     * @return: java.lang.String
     **/
    @GetMapping("/download")
    public String downloadFile(HttpServletResponse response,String filePath)  {
        fileUpload.accessFile(response,minioYml.getBucketName(),minioYml.getUploadPath()+SpringMvcFileUtils.getFileName(filePath),"attachment");
        return null;
    }

    /**
     * @Description: 文件预览
     * @CreateDate: 2021/01/15 13:59:21
     * @param response: 响应对象
     * @param filePath: 文件路径
     * @return: java.lang.String
     **/
    @GetMapping("/preview")
    public String previewFile(HttpServletResponse response,String filePath)  {
        fileUpload.accessFile(response,minioYml.getBucketName(),minioYml.getUploadPath()+SpringMvcFileUtils.getFileName(filePath),"inline");
        return null;
    }

    /**
     * @Description: 文件删除
     * @CreateDate: 2021/01/14 15:32:07
     * @param fileId: 文件id
     * @return: java.lang.String
     **/
    @PostMapping(value = "/delete")
    public String delete(@RequestParam("fileId") Long fileId) {
        try {
            FileInfo fileInfo = minioFileUploadService.getOne(fileId);
            fileUpload.deleteFile(minioYml.getBucketName(), minioYml.getUploadPath() + fileInfo.getFileSaveName());
            minioFileUploadService.delete(fileInfo);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return FileState.FILE_DELETE_ERROR.getValue();
        }
        return FileState.FILE_DELETE_OK.getValue();
    }

}