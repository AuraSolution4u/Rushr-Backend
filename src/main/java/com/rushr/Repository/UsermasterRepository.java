package com.rushr.Repository;

import com.rushr.Entity.Chapter;
import com.rushr.Entity.Usermaster;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsermasterRepository extends JpaRepository<Usermaster,Long> {

    Usermaster findByUserId(Long id);
    Usermaster findByEmailId(String emailId);
    List<Usermaster> findByChapterId(Chapter chapterId);


}
