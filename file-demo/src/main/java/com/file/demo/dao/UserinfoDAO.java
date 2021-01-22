package com.file.demo.dao;


import com.file.demo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserinfoDAO extends JpaRepository<UserInfo, Long> {

}
