package com.minio.demo;

import com.minio.common.ResponseResult;
import com.file.dao.FileInfoDAO;
import com.file.dao.FileTypeDAO;
import com.file.dao.UserinfoDAO;
import com.file.entity.FileInfo;
import com.file.entity.FileType;
import com.file.entity.UserInfo;
import com.file.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MinioUtilApplicationTests {

    @Resource
    private UserinfoDAO userinfoDAO;

    @Resource
    private FileTypeDAO fileTypeDAO;

    @Resource
    private FileInfoDAO fileInfoDAO;


    public void main() {
        save();
    }
    @Test
    public void save() {
        for (long i = 20; i < 25; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("zs"+i);
            userInfo.setPassword("123456");
            userInfo.setPhone(1385654144);
            userInfo.setRegisterTime(DateUtils.getSystemCurrentDate());
            userInfo.setStatus(1);
            userinfoDAO.save(userInfo);

            FileType fileType = new FileType();
            fileType.setFileTypeName("文档"+i);
            fileType.setFileTypeStatus(1);
            fileTypeDAO.save(fileType);

            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName("文件名" + i);
            fileInfo.setFileSaveName("保存名" +i);
            fileInfo.setFileSavePath("路径"+i);
            fileInfo.setFileSize(i);
            fileInfo.setFileStatus(1);
            fileInfo.setFileType(fileType);
            fileInfo.setUser(userInfo);
            fileInfo.setFileUploadTime(DateUtils.getSystemCurrentDate());
            fileInfoDAO.save(fileInfo);
        }
    }

    @Test
    public void HttpStatus() {
        HttpStatus internalServerError = HttpStatus.NO_CONTENT;
        int value = internalServerError.value();
        String str = internalServerError.toString();
        String reasonPhrase = internalServerError.getReasonPhrase();
        String name = internalServerError.name();

        System.out.println("value=" +value);
        System.out.println("str=" +str);
        System.out.println("reasonPhrase=" +reasonPhrase);
        System.out.println("name=" +name);

        ResponseResult responseResult = new ResponseResult();
        responseResult.put("code",1212);
        List<ResponseResult> responseResults= null;
        Optional.ofNullable(responseResults).ifPresent(p -> responseResults.add(responseResult));
        List<ResponseResult> responseResults1= new ArrayList<>();
        Optional.ofNullable(responseResults1).ifPresent(p -> responseResults1.add(responseResult));

        System.out.println("responseResults=" +responseResults);
        System.out.println("responseResults1=" +responseResults1);


    }

    @Test
    public void test() {
        //格式化格式
        String format = "YYYY-MM-dd hh:mm:ss";
        // DateTimeFormatter.ofPattern方法根据指定的格式输出时间
        String formatDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));

        System.out.println("instant" +formatDateTime);
    }

}
