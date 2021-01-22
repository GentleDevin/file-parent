package com.file.demo.service;

import com.file.demo.dao.UserinfoDAO;
import com.file.demo.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Title:  用户信息服务层
 * @Description: 处理用户信息业务逻辑
 * @Author: Devin
 * CreateDate: 2021/1/13 16:42
 */

@Service
public class UserinfoService {
    @Resource
    private UserinfoDAO userinfoDAO;

    public void save(UserInfo userInfo) {
        userinfoDAO.save(userInfo);
    }

}
