package com.file.demo.service;


import com.file.commons.common.FileInfoResult;
import com.file.commons.common.ResponseResult;
import com.file.demo.dao.FileInfoDAO;
import com.file.demo.entity.FileInfo;
import com.file.demo.entity.FileType;
import com.file.demo.entity.UserInfo;
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
public class MinioFileUploadService {

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
     * @Description: 删除对象
     * param fileInfo: 文件信息
     * @CreateDate: 2021/01/14 18:31:32
     **/
    public void delete(FileInfo fileInfo) {
        fileInfoDAO.delete(fileInfo);
    }

}
