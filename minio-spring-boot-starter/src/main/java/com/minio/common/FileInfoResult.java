package com.minio.common;

import lombok.Data;

/**
 * @Title: 文件信息类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/13 16:32:36
 **/
@Data
public class FileInfoResult {
  /**
   * 上传文件名称
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
