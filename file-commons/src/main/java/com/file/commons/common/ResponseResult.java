package com.file.commons.common;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: 返回结果信息
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/13 13:47
 */
public class ResponseResult  extends HashMap<String, Object> {
        private static final long serialVersionUID = 1L;

        public ResponseResult() {
            put("code", 0);
            put("msg", "success");
        }

        public static ResponseResult error() {
            return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知异常，请联系管理员");

        }

        public static ResponseResult error(String msg) {
            return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
        }

        public static ResponseResult error(int code, String msg) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.put("code", code);
            responseResult.put("msg", msg);
            return responseResult;
        }

        public static ResponseResult ok(String msg) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.put("msg", msg);
            return responseResult;
        }

        public static ResponseResult ok(Map<String, Object> map) {
            ResponseResult responseResult = new ResponseResult();
            responseResult.putAll(map);
            return responseResult;
        }

        public static ResponseResult ok() {
            return new ResponseResult();
        }


        @Override
        public ResponseResult put(String key, Object value) {
            super.put(key, value);
            return this;
        }
    
}
