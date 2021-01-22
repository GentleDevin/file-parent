package com.file.local.utils;

import com.file.commons.common.FileInfoResult;
import com.file.commons.common.ResultCommon;
import com.file.commons.enums.FileState;
import com.file.commons.utils.io.SpringMvcFileUtils;
import com.file.local.autoconfig.yml.LocalFileUploadYml;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 10:37
 */
public class LocalFileUploadUtil {

    /**
     * @Description:  本地文件上传
     * @param
     * @param file : 文件类
     * @param request : request请求对象
     * @param localFileUploadYml : 本地上传配置文件
     * @param resultCommonList
     * @return: java.util.List<com.minio.common.ResponseResult> 返回上传结果信息
     * @CreateDate: 2021/01/08 17:41:42
     **/
    public static void uploadFile(MultipartFile file, HttpServletRequest request, LocalFileUploadYml localFileUploadYml, List<ResultCommon> resultCommonList) {
        ResultCommon resultCommon = new ResultCommon();
        InputStream is = null;
        try {
            //文件校验
            boolean validationResult = SpringMvcFileUtils.fileValidation(file, resultCommon);
            if (validationResult) {
                //初始化文件信息
                FileInfoResult fileInfo = new FileInfoResult();
                SpringMvcFileUtils.initFileInfo(file, fileInfo);
                is = file.getInputStream();
                //文件路径
                String filePath = localFileUploadYml.getUploadPath() + fileInfo.getFileSaveName();
                //上传文件
                FileUtils.copyInputStreamToFile(is, new File(filePath));
                //获取访问URL
                fileInfo.setFileSavePath(SpringMvcFileUtils.getFileAccessUrl(request,localFileUploadYml.getAccessPath(),fileInfo.getFileSaveName()));
                resultCommon.setCode(HttpStatus.OK.value());
                resultCommon.setMsg(HttpStatus.OK.getReasonPhrase());
                resultCommon.setData(fileInfo);
            }else{
                resultCommon.setCode(HttpStatus.NO_CONTENT.value());
                resultCommon.setMsg(HttpStatus.NO_CONTENT.getReasonPhrase());
                resultCommon.setData(null);
            }
        } catch (Exception e) {
            resultCommon.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            resultCommon.setMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            resultCommon.setData(null);
            e.printStackTrace();
        }finally {
            //关流
            IOUtils.closeQuietly(is);
        }
        resultCommonList.add(resultCommon);
    }


    /**
     * @Description: 文件下载
     * @param response: 响应对象
     * @param fileName: 文件路径
     * @param openStyle 浏览器打开方式，在线预览/下载
     * @return: com.minio.common.FileInfo
     * @CreateDate: 2021/01/13 17:42:21
     **/
    @SneakyThrows
    public static String accessFile(HttpServletResponse response,String uploadPath,String fileName, String openStyle) {
        ResultCommon resultCommon = new ResultCommon();
        File file = new File(uploadPath+fileName);
        BufferedInputStream bis = null;
        ServletOutputStream os = null;
        if(file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                response.setContentType(MediaType.ALL_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, openStyle + ";" + "filename=" + URLEncoder.encode(fileName, "UTF-8"));
                os = response.getOutputStream();
                IOUtils.copy(bis, os);
                resultCommon.setCode(HttpStatus.OK.value());
                resultCommon.setMsg(HttpStatus.OK.getReasonPhrase());
            } catch (Exception e) {
                resultCommon.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                resultCommon.setMsg(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(bis);
                IOUtils.closeQuietly(os);
            }
    }
        return resultCommon.toString();
}

    public static String deleteFile(String uploadPath,String fileName) {
        File file = new File(uploadPath+fileName);
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            e.printStackTrace();
            return FileState.FILE_DELETE_ERROR.getValue();
        }
        return FileState.FILE_DELETE_OK.getValue();
    }
}
