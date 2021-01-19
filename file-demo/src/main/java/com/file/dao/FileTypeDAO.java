package com.file.dao;


import com.file.entity.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FileTypeDAO extends JpaRepository<FileType, Long> {

}
