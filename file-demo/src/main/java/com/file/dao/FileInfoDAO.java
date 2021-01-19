package com.file.dao;


import com.file.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileInfoDAO extends JpaRepository<FileInfo, Long> {

}
