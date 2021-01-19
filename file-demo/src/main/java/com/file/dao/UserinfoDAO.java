package com.file.dao;


import com.file.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserinfoDAO extends JpaRepository<UserInfo, Long> {

}
