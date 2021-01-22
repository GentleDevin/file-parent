package com.file.demo.controller;


import com.file.api.strategy.FileUploadFactory;
import com.file.api.strategy.IFileUpload;
import com.file.commons.common.ResultCommon;
import com.file.commons.enums.FileState;
import com.file.demo.entity.FileInfo;
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
import java.util.List;

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

    private IFileUpload fileUpload = FileUploadFactory.getFileUploadByKey(FileUploadFactory.LOCAL);


    /**
     * @Description: 文件上传
     * @CreateDate: 2021/01/11 10:03:45
     * @param files: 多个file文件
     * @return: java.lang.String
     **/
    @PostMapping("/upload")
    public String uploadFile(MultipartFile[] files, HttpServletRequest request) {
        List<ResultCommon> resultCommonList = null;
        try {
           resultCommonList = fileUpload.uploadFile(files,request,localFileUploadYml);
           localFileUploadService.save(resultCommonList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return FileState.FILE_UPLOAD_ERROR.getValue();
        }
        return resultCommonList.toString();
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
        return fileUpload.accessFile(response,localFileUploadYml.getUploadPath(),fileName,"attachment");
    }

    /**
     * @Description: 文件预览
     * @CreateDate: 2021/01/15 13:59:21
     * @param response: 响应对象
     * @param fileName: 文件名称
     * @return: java.lang.String
     **/
    @GetMapping("/preview")
    public String previewFile(HttpServletResponse response,String fileName)  {
        return fileUpload.accessFile(response,localFileUploadYml.getUploadPath(),fileName,"inline");
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
            FileInfo fileInfo = localFileUploadService.getOne(fileId);
            fileUpload.deleteFile(localFileUploadYml.getUploadPath(),fileInfo.getFileSaveName());
            localFileUploadService.delete(fileInfo);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return FileState.FILE_DELETE_ERROR.getValue();
        }
        return FileState.FILE_DELETE_OK.getValue();
    }

}