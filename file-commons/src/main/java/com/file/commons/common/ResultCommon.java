package com.file.commons.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/13 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultCommon<T> {
    private int code;
    private String msg;
    private T data;
}
