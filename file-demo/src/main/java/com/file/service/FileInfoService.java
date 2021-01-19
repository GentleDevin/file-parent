package com.file.service;


import com.minio.autoconfig.yml.MinioYml;
import com.minio.common.FileInfoResult;
import com.minio.common.ResponseResult;
import com.file.dao.FileInfoDAO;
import com.file.entity.FileInfo;
import com.file.entity.FileType;
import com.file.entity.UserInfo;
import com.minio.utils.MinioUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Title: 文件信息服务层
 * @Description: 处理文件类型业务逻辑
 * @Author: Devin
 * CreateDate: 2021/1/13 16:42
 */
@Service
public class FileInfoService {

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
     * @param responseResult:
     * @Description: 保存文件信息
     * @Author: Devin
     * @CreateDate: 2021/01/13 17:06:05
     * @return: void
     **/
    public void save(List<ResponseResult> responseResult) {
        for (ResponseResult result : responseResult) {
            if (null != result.get("data")) {
                FileInfoResult fileInfoResult = (FileInfoResult) result.get("data");
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileType(new FileType(1L));
                fileInfo.setUser(new UserInfo(1L));
                BeanUtils.copyProperties(fileInfoResult, fileInfo);
                fileInfoDAO.save(fileInfo);
            }
        }
    }

    /**
     * @param minioYml:  配置文件
     * @param minioUtil: 工具类
     * @param fileIds:   主键id
     * @Description: 删除对象
     * @CreateDate: 2021/01/14 18:31:32
     * @return: com.minio.entity.FileInfo
     **/
    public void delete(MinioYml minioYml, MinioUtil minioUtil, List<Long> fileIds) {
        List<FileInfo> fileInfoList = findAllById(fileIds);
        fileInfoList.stream().forEach(fileInfo -> {
            boolean result = minioUtil.removeObject(minioYml.getBucketName(), minioYml.getConfigDir() + fileInfo.getFileSaveName());
            if (result) {
                fileInfoDAO.delete(fileInfo);
            }
        });
    }

}
