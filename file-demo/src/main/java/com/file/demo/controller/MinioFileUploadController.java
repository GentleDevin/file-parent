package com.file.demo.controller;


import com.file.demo.service.MinioFileUploadService;
import com.file.minio.autoconfig.yml.MinioYml;
import com.file.minio.utils.MinioUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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


    /**
     * @Description: 文件上传
     * @CreateDate: 2021/01/11 10:03:45
     * @param files: 多个file文件
     * @return: java.lang.String
     **/
    @PostMapping("/upload")
    public String uploadFile(MultipartFile[] files) {
        return minioFileUploadService.multiUploadFile(files,minioYml);
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
        minioFileUploadService.accessFile(response,minioYml,filePath,"attachment");
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
        minioFileUploadService.accessFile(response,minioYml,filePath,"inline");
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
        return minioFileUploadService.delete(fileId,minioYml);
    }
}