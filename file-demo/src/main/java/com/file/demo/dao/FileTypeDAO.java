package com.file.demo.dao;


import com.file.demo.entity.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileTypeDAO extends JpaRepository<FileType, Long> {

}
