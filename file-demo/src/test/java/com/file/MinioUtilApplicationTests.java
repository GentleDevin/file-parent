package com.file;

import com.file.demo.dao.FileInfoDAO;
import com.file.demo.dao.FileTypeDAO;
import com.file.demo.dao.UserinfoDAO;
/*import com.minio.utils.UtilFuns;*/
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MinioUtilApplicationTests {

    @Resource
    private UserinfoDAO userinfoDAO;

    @Resource
    private FileTypeDAO fileTypeDAO;

    @Resource
    private FileInfoDAO fileInfoDAO;

    @Test
    public void main() {
        save();
    }

    public void save() {
        /*for (int i = 20; i < 25; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName("zs"+i);
            userInfo.setPassword("123456");
            userInfo.setPhone(1385654144);
            userInfo.setRegisterTime(UtilFuns.getSystemTimeByUtilDate());
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
            fileInfo.setFileUploadTime(UtilFuns.getSystemTimeByUtilDate());
            fileInfoDAO.save(fileInfo);
        }*/
    }
}
