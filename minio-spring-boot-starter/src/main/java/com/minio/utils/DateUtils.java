package com.minio.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Title: 日期工具类
 * @Description:
 * @Author: Devin 
 * @CreateDate: 2021/01/14 13:40:58
 **/
public class DateUtils {

	public static String getSystemCurrentDate() {
		//格式化格式
		String format = "YYYY-MM-dd hh:mm:ss";
		// DateTimeFormatter.ofPattern方法根据指定的格式输出时间
		String formatDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
		return formatDateTime;
	}
}
