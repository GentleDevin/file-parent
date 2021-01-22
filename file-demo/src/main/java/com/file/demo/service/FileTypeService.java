package com.file.demo.service;

import com.file.demo.dao.FileTypeDAO;
import com.file.demo.entity.FileType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Title: 文件类型服务层
 * @Description: 处理文件类型业务逻辑
 * @Author: Devin
 * CreateDate: 2021/1/13 16:42
 */
@Service
public class FileTypeService {
    @Resource
    private FileTypeDAO fileTypeDAO;

    public void save(FileType fileType) {
         fileTypeDAO.save(fileType);
    }

}
