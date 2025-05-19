package com.rushr.Repository;

import com.rushr.Entity.GreekAlphabetsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GreekAlphabetsMasterRepository extends JpaRepository<GreekAlphabetsMaster,Long> {


}
