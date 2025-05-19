package com.rushr.Repository;

import com.rushr.Entity.OTPGeneration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPGenerationRepository extends JpaRepository<OTPGeneration,String> {
//
//    OTPGeneration findByUserId(Long userId);

    OTPGeneration findByEmailId(String emailId);
}
