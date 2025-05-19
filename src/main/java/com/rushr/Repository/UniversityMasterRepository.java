package com.rushr.Repository;

import com.rushr.Entity.UniversityMaster;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityMasterRepository extends JpaRepository<UniversityMaster,Long> {

    UniversityMaster findByUniversityId(long universityId);



}
