package com.rushr.Repository;

import com.rushr.Entity.ChapterRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepoRepository extends JpaRepository<ChapterRepo,Long> {

    List<ChapterRepo> findByUserId(Long userId);

    List<ChapterRepo> findByChapterId(Long chapterId);
}
