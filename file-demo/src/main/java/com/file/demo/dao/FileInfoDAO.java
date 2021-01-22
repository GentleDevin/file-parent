package com.file.demo.dao;


import com.file.demo.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileInfoDAO extends JpaRepository<FileInfo, Long> {

}
