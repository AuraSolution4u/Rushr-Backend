package com.rushr.Repository;

import com.rushr.Entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChapterCreationRepository extends JpaRepository<Chapter,Long> {

    Chapter findByChapterId(Long id);

}
