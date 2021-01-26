package com.file.demo.controller;


import com.file.demo.service.LocalFileUploadService;
import com.file.local.autoconfig.yml.LocalFileUploadYml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: 本地文件上传信息控制类
 * @Description: 提供文件上传、下载、删除
 * @Author: Devin
 * @CreateDate: 2021/01/12 17:27:10
 **/
@RestController
@RequestMapping("/local")
public class LocalFileUploadController {
    private static Logger logger= LogManager.getLogger(LocalFileUploadController.class.getName());

    @Resource
    private LocalFileUploadService localFileUploadService;
    @Autowired
    private LocalFileUploadYml localFileUploadYml;


    /**
     * @Description: 文件上传
     * @CreateDate: 2021/01/11 10:03:45
     * @param files: 多个file文件
     * @return: java.lang.String
     **/
    @PostMapping("/upload")
    public String uploadFile(MultipartFile[] files, HttpServletRequest request) {
        return localFileUploadService.multiUploadFile(files,request,localFileUploadYml);
    }

    /**
     * @Description: 文件下载
     * @CreateDate: 2021/01/12 17:26:56
     * @param response: 响应对象
     * @param fileName: 文件名称
     * @return: java.lang.String
     **/
    @GetMapping("/download")
    public String downloadFile(HttpServletResponse response,String fileName)  {
        return localFileUploadService.accessFile(response,localFileUploadYml.getUploadPath(),fileName,"attachment");
    }

    /**
     * @Description: 文件预览
     * @CreateDate: 2021/01/15 13:59:21
     * @param response: 响应对象
     * @param fileName: 文件名称
     * @return: java.lang.String
     **/
    @GetMapping("/preview")
    public String previewFile(HttpServletResponse response,String fileName) {
        return localFileUploadService.accessFile(response,localFileUploadYml.getUploadPath(),fileName,"inline");
    }

    /**
     * @Description: 文件删除
     * @CreateDate: 2021/01/14 15:32:07
     * @param fileId: 文件id
     * @return: java.lang.String
     **/
    @PostMapping(value = "/delete")
    public String delete(@RequestParam("fileId") Long fileId) {
        return  localFileUploadService.delete(fileId,localFileUploadYml.getUploadPath());
    }
}