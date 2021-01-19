package com.file.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

/**
 * @Title: 文件分类
 * @Description:
 * @Author: Devin
 * @CreateDate: 2021/01/13 16:32:36
 **/

@RequiredArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class FileType {

  /**
   * 主键，文件分类id
   **/
  @NonNull
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_type_id", unique = true, nullable = false)
  private Long fileTypeId;
  /**
   * 文件分类名称
   **/
  private String fileTypeName;
  /**
   * 文件分类状态,1可用，0不可用
   **/
  private Integer fileTypeStatus;

}
