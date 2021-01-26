package com.file.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title: 文件操作枚举类
 * @Description: 包括文件上传、下载、删除
 * @Author: Devin
 * CreateDate: 2021/1/15 11:12
 */
@Getter
@AllArgsConstructor
public enum FileStatus {

    /**
     * 文件操作OK状态码
     **/
    FILE_UPLOAD_OK(200, "文件上传成功"),
    FILE_DOWNLOAD_OK(201, "文件下载成功"),
    FILE_DELETE_OK(202, "文件删除成功"),

    /**
     * 文件操作ERROR状态码
     **/
    FILE_UPLOAD_ERROR(500, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(501, "文件下载成功"),
    FILE_DELETE_ERROR(502,"文件删除失败");

    private int key;
    private String value;

}
