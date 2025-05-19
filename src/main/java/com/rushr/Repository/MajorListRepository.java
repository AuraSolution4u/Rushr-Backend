package com.rushr.Repository;

import com.rushr.Entity.MajorsListMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorListRepository extends JpaRepository<MajorsListMaster,Long> {

    MajorsListMaster findById(long Id);
}
