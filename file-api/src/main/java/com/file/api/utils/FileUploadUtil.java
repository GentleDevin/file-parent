/*
package com.file.utils;

import com.file.common.FileInfoResult;
import com.file.common.ResponseResult;
import com.file.common.ResultCommon;
import com.file.utils.date.DateUtils;
import io.minio.ObjectStat;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

*/
/**
 * @Title: 文件存储工具类
 * @Description: 封装文件上传、下载、删除等操作
 * @Author: Devin
 * @CreateDate: 2021/01/21 09:14:46
 **//*

public class FileUploadUtil {

    */
/**
     * @Title:  文件上传校验
     * @Description: 校验文件为空、大小、格式
     * @Author: Devin
     * @CreateDate: 2021/01/21 09:43:46
     * @return: void
     **//*

    private boolean fileValidation(MultipartFile file ,List<ResultCommon> resultCommon) {
        ResponseResult responseResult = new ResponseResult();
        if (file.isEmpty() || file.getSize() == 0) {
            responseResult.put("code", HttpStatus.NO_CONTENT.value());
            responseResult.put("msg", HttpStatus.NO_CONTENT.getReasonPhrase());
            responseResult.put("data", null);
            responseResults.add(responseResult);
            return false;
        }
        return true;
    }



    */
/**
     * @Description: 文件上传
     * @param bucketName: 桶名称
     * @param file: 文件类
     * @param configDir: 文件配置路径
     * @param responseResults: 上传结果信息集合
     * @return: java.util.List<com.minio.common.ResponseResult> 返回上传结果信息
       @CreateDate: 2021/01/08 17:41:42
     **//*

    public List<ResponseResult> fileUpload(MultipartFile file,String bucketName, String configDir,List<ResponseResult> responseResults) {
        ResponseResult responseResult = new ResponseResult();
        if (file.isEmpty() || file.getSize() == 0) {
            responseResult.put("code", HttpStatus.NO_CONTENT.value());
            responseResult.put("msg", HttpStatus.NO_CONTENT.getReasonPhrase());
            responseResult.put("data", null);
            responseResults.add(responseResult);
            return responseResults;
        }
        try {
            //文件是否存在
           */
/* if (!bucketExists(bucketName)) {
                makeBucket(bucketName);
            }*//*

            FileInfoResult fileInfo = new FileInfoResult();
            initFileInfo(file,fileInfo);
            InputStream inputStream = file.getInputStream();
            //文件名称(相对路径)
            String objectName = configDir+fileInfo.getFileSaveName();
*/
/*
            putObject(bucketName, objectName, inputStream);
*//*

            inputStream.close();
*/
/*
            fileInfo.setFileSavePath(getObjectUrl(bucketName, objectName));
*//*

            responseResult.put("code", HttpStatus.OK.value());
            responseResult.put("msg", HttpStatus.OK.getReasonPhrase());
            responseResult.put("data",fileInfo);
            responseResults.add(responseResult);
            return responseResults;
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseResult.put("msg", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            responseResult.put("data", null);
            responseResults.add(responseResult);
            return responseResults;
        }
    }

    */
/**
     * @Description: 多文件上传
     * @CreateDate: 2021/01/13 11:33:03
     * @param file: 文件类
     * @param bucketName: 桶名称
     * @param configDir: 文件配置路径
     * @return: java.lang.String
     **//*

    public List<ResponseResult>  multiFileUpload(MultipartFile[] file,String bucketName, String configDir) {
        List<ResponseResult> responseResults = new ArrayList<>();
        for (MultipartFile multipartFile: file) {
            fileUpload(multipartFile,bucketName,configDir,responseResults);
        }
        return responseResults;
    }

    */
/**
     * @Description: 初始化文件信息类
     * @param file: 文件类
     * @param fileInfo: 文件信息类
     * @return: com.minio.common.FileInfo
     * @CreateDate: 2021/01/13 17:42:21
     **//*

    public FileInfoResult initFileInfo(MultipartFile file, FileInfoResult fileInfo) {
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setFileSize(file.getSize());
        fileInfo.setFileUploadTime(DateUtils.getSystemCurrentDate());
        fileInfo.setFileSaveName(UUID.randomUUID().toString().replaceAll("-", "")
                + fileInfo.getFileName().substring(fileInfo.getFileName().lastIndexOf(".")));
        fileInfo.setFileStatus(1);
        return fileInfo;
    }

    */
/**
     * @Description: 文件下载
     * @param bucketName: 文件类
     * @param fileName: 文件信息类
     * @return: com.minio.common.FileInfo
     * @CreateDate: 2021/01/13 17:42:21
     **//*

    @SneakyThrows
    public  void download(String bucketName, String fileName, HttpServletResponse response,String openStyle) {
        //获取对象的元数据
        final ObjectStat stat = minioClient.statObject(bucketName, fileName);
        if(openStyle.equals("attachment")) {
            response.setContentType(stat.contentType());
        }else{
            // 在浏览器中打开
            response.setContentType(MediaType.ALL_VALUE);
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, openStyle+";"+"filename=" + URLEncoder.encode(fileName, "UTF-8"));
        InputStream is = minioClient.getObject(bucketName, fileName);
        ServletOutputStream os = response.getOutputStream();
        IOUtils.copy(is, os);
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }
}*/
