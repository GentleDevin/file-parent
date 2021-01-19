package com;

import com.file.autoconfig.FileStoreSelector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Title:
 * @Description:
 * @Author: Devin 
 * @CreateDate: 2021/01/19 14:37:20
 **/
@FileStoreSelector(fileStoreWay="")
@SpringBootApplication
public class FileDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileDemoApplication.class, args);
    }

}