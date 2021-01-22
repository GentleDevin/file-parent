package com.file.api.strategy;

import com.file.local.autoconfig.yml.LocalFileUploadYml;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/20 10:30
 */
public interface IFileUpload<T> {


    default List<T> uploadFile(MultipartFile[] files, HttpServletRequest request, LocalFileUploadYml localFileUploadYml) { return null; }

    default List<T> uploadFile(MultipartFile[] files, String uploadPath, String fileName) { return null; }

    String accessFile(HttpServletResponse response, String uploadPath,String fileName,String openStyle);

    String deleteFile(String uploadPath,String fileName);
}
