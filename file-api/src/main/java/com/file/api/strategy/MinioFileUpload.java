package com.file.api.strategy;

import com.file.commons.common.ResponseResult;
import com.file.minio.utils.MinioUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: minio文件上传实现类
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 10:37
 */
public class MinioFileUpload implements IFileUpload {

    @Override
    public List<ResponseResult> uploadFile(MultipartFile[] files, String uploadPath, String fileName) {
        return MinioUtil.multiFileUpload(files,uploadPath, fileName);
    }

    @Override
    public String accessFile(HttpServletResponse response, String uploadPath, String fileName, String openStyle) {
        MinioUtil.accessFile(response,uploadPath,fileName,openStyle);
         return null;
    }

    @Override
    public String deleteFile(String uploadPath, String fileName) {
         MinioUtil.removeObject(uploadPath,fileName);
        return null;


    }


}
