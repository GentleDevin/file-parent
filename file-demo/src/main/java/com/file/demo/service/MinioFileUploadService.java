package com.file.demo.service;


import com.file.commons.common.FileInfoResult;
import com.file.commons.common.ResponseResult;
import com.file.commons.enums.FileStatus;
import com.file.commons.utils.io.SpringMvcFileUtils;
import com.file.demo.dao.FileInfoDAO;
import com.file.demo.entity.FileInfo;
import com.file.demo.entity.FileType;
import com.file.demo.entity.UserInfo;
import com.file.minio.autoconfig.yml.MinioYml;
import com.file.minio.utils.MinioUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * @Title: 文件信息服务层
 * @Description: 处理文件类型业务逻辑
 * @Author: Devin
 * CreateDate: 2021/1/13 16:42
 */
@Service
public class MinioFileUploadService {
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
     * @param fileInfo:
     * @Description: 保存文件信息
     * @Author: Devin
     * @CreateDate: 2021/01/13 17:06:05
     * @return: void
     **/
    public void save(FileInfo fileInfo) {
        fileInfo.setFileType(new FileType(1L));
        fileInfo.setUser(new UserInfo(1L));
        fileInfoDAO.save(fileInfo);
    }

    /**
     * @Description: 删除对象
     * param fileInfo: 文件信息
     * @CreateDate: 2021/01/14 18:31:32
     **/
    public void delete(FileInfo fileInfo) {
        fileInfoDAO.delete(fileInfo);
    }

    /**
     * @Description: 删除文件和文件信息
     * @CreateDate: 2021/01/25 14:24:14
     * @param fileId: 
     * @param minioYml: 
     * @return: java.lang.String
     **/
    public String delete(Long fileId,MinioYml minioYml) {
        ResponseResult responseResult = new ResponseResult();
        try {
            FileInfo fileInfo = getOne(fileId);
            boolean result = MinioUtil.removeObject(minioYml.getBucketName(), minioYml.getUploadPath() + fileInfo.getFileSaveName());
            if(result) {
                responseResult = ResponseResult.getResponseResult(FileStatus.FILE_DELETE_OK.getKey(), FileStatus.FILE_DELETE_OK.getValue());
                delete(fileInfo);
            }
        }catch (Exception e) {
            logger.error(e.getMessage());
            responseResult= ResponseResult.getResponseResult(FileStatus.FILE_DELETE_ERROR.getKey(),FileStatus.FILE_DELETE_ERROR.getValue());
        }
        return responseResult.toString();
    }

    /**
     * @Description: 单个文件上传
     * @CreateDate: 2021/01/25 14:02:27
     * @param file:
     * @param minioYml:
     * @return: java.lang.String
     **/
    public ResponseResult uploadFile(MultipartFile file,MinioYml minioYml) {
        FileInfoResult fileInfoResult = null;
        ResponseResult responseResult = new ResponseResult();
        try {
            //校验文件是否合法
            boolean validationResult = SpringMvcFileUtils.fileValidation(file, responseResult);
            if (validationResult) {
                //上传文件
                MinioUtil.uploadFile(file, minioYml.getBucketName(), minioYml.getUploadPath(),responseResult);
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
     * @Description: 多个文件上传
     * @CreateDate: 2021/01/25 14:02:27
     * @param files:
     * @param minioYml:
     * @return: java.lang.String
     **/
    public String multiUploadFile(MultipartFile[] files,MinioYml minioYml) {
        List<ResponseResult> responseResultList = new ArrayList<>();
        for (MultipartFile file : files) {
            ResponseResult responseResult = uploadFile(file, minioYml);
            responseResultList.add(responseResult);
        }
        return responseResultList.toString();
    }

    /**
     * @Description: 文件下载/预览
     * @CreateDate: 2021/01/25 14:11:25
     * @param response:
     * @param minioYml:
     * @param filePath:
     * @param openStyle:
     * @return: void
     **/
    public void accessFile(HttpServletResponse response,MinioYml minioYml,String filePath,String openStyle) {
        MinioUtil.accessFile(response,minioYml.getBucketName(),minioYml.getUploadPath()+ SpringMvcFileUtils.getFileName(filePath),openStyle);
    }
}
