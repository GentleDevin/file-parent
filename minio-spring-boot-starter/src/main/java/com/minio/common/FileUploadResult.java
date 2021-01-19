package com.minio.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/13 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResult<T> {
    private int code;
    private String msg;
    private T data;
}
