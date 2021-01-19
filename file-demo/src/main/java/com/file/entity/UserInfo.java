package com.file.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * @Title: 用户实体类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/08 17:15:44
 **/

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserInfo {

    /**
     * 主键，用户信息id
     **/
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;
    /**
     * 用户名称
     **/
    private String userName;
    /**
     * 用户密码
     **/
    private String password;
    /**
     * 手机号码
     **/
    private Integer phone;
    /**
     * 注册时间
     **/
    private String registerTime;
    /**
     * 用户信息状态,1可用，0不可用
     **/
    private Integer status;
}
