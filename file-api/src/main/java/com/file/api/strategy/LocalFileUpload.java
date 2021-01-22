package com.file.api.strategy;

import com.file.commons.common.ResultCommon;
import com.file.local.autoconfig.yml.LocalFileUploadYml;
import com.file.local.utils.LocalFileUploadUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: 本地文件上传实现类
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 10:37
 */
public class LocalFileUpload implements IFileUpload {

    @Override
    public List<ResultCommon> uploadFile(MultipartFile[] files,HttpServletRequest request,LocalFileUploadYml localFileUploadYml) {
        List<ResultCommon> resultCommonList = new ArrayList<>();
        for (MultipartFile file: files) {
            LocalFileUploadUtil.uploadFile(file,request, localFileUploadYml,resultCommonList);
        }
        return resultCommonList;
    }

    @Override
    public String accessFile(HttpServletResponse response, String uploadPath,String fileName, String openStyle) {
        return LocalFileUploadUtil.accessFile(response, uploadPath,fileName,openStyle);
    }

    @Override
    public String deleteFile(String uploadPath, String fileName) {
        return LocalFileUploadUtil.deleteFile(uploadPath, fileName);
    }

}
