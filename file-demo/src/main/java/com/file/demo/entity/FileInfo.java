package com.file.demo.entity;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @Title: 文件信息类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/13 16:32:36
 **/
@Data
@Entity
@Proxy(lazy = false)
public class FileInfo {
  /**
   * 主键，文件id
   **/
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long fileId;
  /**
   * 外键，文件分类
   **/
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "file_type_id")
  private FileType fileType;
  /**
   * 外键，用户信息类
   **/
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private UserInfo user;
  /**
   * 文件名称
   **/
  private String fileName;
  /**
   * 文件大小
   **/
  private Long fileSize;
  /**
   * 文件上传时间
   **/
  private String fileUploadTime;
  /**
   * 文件保存名称
   **/
  private String fileSaveName;
  /**
   * 文件保存路径
   **/
  private String fileSavePath;
  /**
   * 文件状态,1可用，0不可用
   **/
  private Integer fileStatus;


}
