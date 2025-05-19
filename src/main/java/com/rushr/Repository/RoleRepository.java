package com.rushr.Repository;

import com.rushr.Entity.Chapter;
import com.rushr.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRoleNameAndChapterId(String roleName, Chapter chapterId);

    List<Role> findByChapterId(Chapter chapterId);

    Role findByRoleId(Long roleId);
}
