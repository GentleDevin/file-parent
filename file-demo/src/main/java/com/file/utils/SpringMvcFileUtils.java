package com.file.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;

/**
 * @Title:
 * @Description:
 * @Author: Devin
 * CreateDate: 2021/1/14 14:56
 */
public class SpringMvcFileUtils {
    private static Logger logger= LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);



    /**
     * 下载文件
     *
     * @param filePath
     *            文件的路径
     * @param fileReName
     *            文件的下载显示的别名
     * @return 文件的对象
     * @throws IOException
     */
    public static ResponseEntity<byte[]> downloadFile(String filePath, String fileReName) throws IOException {
        // 指定文件,必须是绝对路径
        File file = new File(filePath);
        // 下载浏览器响应的那个文件名
        String dfileName = new String(fileReName.getBytes("GBK"), "iso-8859-1");
        // 下面开始设置HttpHeaders,使得浏览器响应下载
        HttpHeaders headers = new HttpHeaders();
        // 设置响应方式
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 设置响应文件
        headers.setContentDispositionFormData("attachment", dfileName);
        // 把文件以二进制形式写回
        ResponseEntity<byte[]> result = null;
        try {
            result = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return result;
    }


    /**
     * 下载文件
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String downloadFiles(HttpServletResponse response, String fileName){
        if (!StringUtils.hasLength(fileName)) {
            return "文件名称为空";
        }
        File file = new File(fileName);
        response.setHeader("content-type", "application/octet-stream");
        // 设置强制下载不打开
        response.setContentType("application/force-download");
        // 设置文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

        byte[] buffer = new byte[1024];
        InputStream fis = null;
        BufferedInputStream bis = null;

        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "文件下载成功";
    }

    /**
     * 判断文件大小
     *
     * @param file  文件
     * @param size  限制大小
     * @param unit  限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(MultipartFile file, int size, String unit) {
        if (file.isEmpty() || StringUtils.isEmpty(size)|| StringUtils.hasLength(unit)) {
            return false;
        }
        long len = file.getSize();
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }


    /**
     * 检查文件的格式
     *
     * @return
     */
    public static boolean checkFileType(MultipartFile file, String[] supprtedTypes) {
        String fileType = file.getContentType();
        logger.info("文件的格式为："+fileType);
        return Arrays.asList(supprtedTypes).contains(fileType);
    }

    /**
     * 判断文件的大小是否符合要求
     *
     * @param maxSize
     *            最大文件
     * @return
     */
    public static boolean checkFileSize(MultipartFile file, long maxSize) {
        logger.info("文件的大小比较："+file.getSize()+",max:"+maxSize);
        return file.getSize() <= maxSize;
    }

    /**
     * @Title: 获取文件名称
     * @Description:
     * @Author: Devin
     * @CreateDate: 2021/01/14 16:49:17
     * @param filePath:
     * @return: java.lang.String
     **/
    public static String getFileName(String filePath) {
        String fileName = null;
       if(StringUtils.hasText(filePath)) {
            File file = new File(filePath);
            fileName = file.getName();
        }
        return fileName;
    }




}
