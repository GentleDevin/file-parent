package com.file.controller;


import com.minio.autoconfig.yml.MinioYml;
import com.minio.common.ResponseResult;
import com.file.enums.FileState;
import com.file.service.FileInfoService;
import com.minio.utils.MinioUtil;
import com.file.utils.SpringMvcFileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: 文件信息控制类
 * @Description: 提供文件上传、下载、删除
 * @Author: Devin
 * @CreateDate: 2021/01/12 17:27:10
 **/
@RestController
public class FileInfoController {
    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Resource
    private FileInfoService fileInfoService;
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
        List<ResponseResult> responseResult = null;
        try {
            responseResult = minioUtil.multiFileUpload(files, minioYml.getBucketName(),minioYml.getConfigDir());
            fileInfoService.save(responseResult);
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
        minioUtil.download(minioYml.getBucketName(),minioYml.getConfigDir()+SpringMvcFileUtils.getFileName(filePath),response,"attachment");
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
        minioUtil.download(minioYml.getBucketName(),minioYml.getConfigDir()+SpringMvcFileUtils.getFileName(filePath),response,"inline");
        return null;
    }

    /**
     * @Description: 文件删除
     * @CreateDate: 2021/01/14 15:32:07
     * @param fileIds: 文件id集合
     * @return: java.lang.String
     **/
    @PostMapping(value = "/delete")
    public String delete(@RequestParam("fileIds") List<Long> fileIds) {
        try {
            fileInfoService.delete(minioYml,minioUtil,fileIds);
        }catch (Exception e) {
            logger.error(e.getMessage());
            return FileState.FILE_DELETE_ERROR.getValue();
        }
        return FileState.FILE_DELETE_OK.getValue();
    }

}