package com.rushr.Repository;

import com.rushr.Entity.SignUpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpTypeRepository extends JpaRepository<SignUpType,Long> {

    SignUpType findById(long id);

}
