package com.file.demo.service;


import com.file.commons.common.FileInfoResult;
import com.file.commons.common.ResponseResult;
import com.file.commons.enums.FileStatus;
import com.file.commons.utils.io.SpringMvcFileUtils;
import com.file.demo.dao.FileInfoDAO;
import com.file.demo.entity.FileInfo;
import com.file.demo.entity.FileType;
import com.file.demo.entity.UserInfo;
import com.file.local.autoconfig.yml.LocalFileUploadYml;
import com.file.local.utils.LocalFileUploadUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * @Title:  本地上传文件信息服务层
 * @Description: 处理本地文件上传业务逻辑
 * @Author: Devin
 * CreateDate: 2021/1/13 16:42
 */
@Service
public class LocalFileUploadService {
    private static Logger logger= LogManager.getLogger(LocalFileUploadService.class.getName());

    @Resource
    private FileInfoDAO fileInfoDAO;


    /**
     * @param id: 主键id
     * @Description: 获取唯一对象
     * @CreateDate: 2021/01/14 18:31:32
     * @return: com.minio.entity.FileInfo
     **/
    public FileInfo getOne(Long id) {
        return fileInfoDAO.getOne(id);
    }

    /**
     * @param ids: 主键id集合
     * @Description: 根据id集合, 获取多个对象
     * @CreateDate: 2021/01/14 18:31:32
     * @return: com.minio.entity.FileInfo
     **/
    public List<FileInfo> findAllById(List<Long> ids) {
        return fileInfoDAO.findAllById(ids);
    }

    /**
     * @Description: 保存文件信息
     * @CreateDate: 2021/01/13 17:06:05
     * @return: void
     **/
    public boolean save(FileInfo fileInfo) {
        try {
            fileInfo.setFileType(new FileType(1L));
            fileInfo.setUser(new UserInfo(1L));
            fileInfoDAO.save(fileInfo);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * @Description: 删除文件对象
     * @Author: Devin 
     * @CreateDate: 2021/01/25 17:16:36
     * @param fileInfo: 
     * @return: boolean
     **/
    public boolean delete(FileInfo fileInfo) {
        try {
            fileInfoDAO.delete(fileInfo);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * @Description: 根据文件id，删除对象及文件信息
     * param fileInfo: 文件信息
     * @CreateDate: 2021/01/14 18:31:32
     **/
    public String delete(Long fileId,String uploadPath) {
        FileInfo fileInfo = getOne(fileId);
        ResponseResult responseResult = null;
        try {
            responseResult = LocalFileUploadUtil.deleteFile(uploadPath, fileInfo.getFileSaveName());
            delete(fileInfo);
            return ResponseResult.getResponseResult(FileStatus.FILE_DELETE_OK.getKey(),FileStatus.FILE_DELETE_OK.getValue()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.getResponseResult(FileStatus.FILE_DELETE_ERROR.getKey(),FileStatus.FILE_DELETE_ERROR.getValue()).toString();
        }
    }


    /**
     * @Description: 上传单个文件
     * @CreateDate: 2021/01/25 11:16:28
     * @param file:
     * @param request:
     * @param localFileUploadYml:
     * @return: java.lang.String
     **/
    public ResponseResult uploadFile(MultipartFile file, HttpServletRequest request, LocalFileUploadYml localFileUploadYml) {
        ResponseResult responseResult = new ResponseResult();
        FileInfoResult fileInfoResult = null;
        try {
            //校验文件是否合法
            boolean validationResult = SpringMvcFileUtils.fileValidation(file, responseResult);
            if (validationResult) {
                //上传文件
                LocalFileUploadUtil.uploadFile(file, request, localFileUploadYml,responseResult);
                //保存文件数据
                fileInfoResult = (FileInfoResult) responseResult.get("data");
                if(StringUtils.isNotEmpty(fileInfoResult.toString())) {
                    FileInfo fileInfo = new FileInfo();
                    BeanUtils.copyProperties(fileInfoResult,fileInfo);
                    save(fileInfo);
                }
                responseResult = responseResult.getResponseResult(FileStatus.FILE_UPLOAD_OK.getKey(),FileStatus.FILE_UPLOAD_OK.getValue(),fileInfoResult);
            }
        } catch (Exception e) {
            responseResult = responseResult.getResponseResult(FileStatus.FILE_UPLOAD_ERROR.getKey(),FileStatus.FILE_UPLOAD_ERROR.getValue(),null);
            e.printStackTrace();
        }
        return responseResult;
    }

    /**
     * @Description: 上传多个文件
     * @CreateDate: 2021/01/25 11:16:28
     * @param files:
     * @param request:
     * @param localFileUploadYml:
     * @return: java.lang.String
     **/
    public String multiUploadFile(MultipartFile[] files, HttpServletRequest request, LocalFileUploadYml localFileUploadYml) {
        List<ResponseResult> responseResultList = new ArrayList<>();
        for (MultipartFile file: files) {
            ResponseResult responseResult = uploadFile(file, request, localFileUploadYml);
            responseResultList.add(responseResult);
        }
        return responseResultList.toString();
    }

    /**
     * @Description: 访问文件：下载/浏览
     * @CreateDate: 2021/01/25 11:16:09
     * @param response: 
     * @param uploadPath: 
     * @param fileName: 
     * @param openStyle: 
     * @return: java.lang.String
     **/
    public String accessFile(HttpServletResponse response, String uploadPath, String fileName, String openStyle) {
        return LocalFileUploadUtil.accessFile(response, uploadPath,fileName,openStyle);
    }
}
