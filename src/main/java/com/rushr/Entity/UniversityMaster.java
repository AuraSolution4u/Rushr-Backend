package com.rushr.Entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RH_UNIVERSITIES_MASTER")
@Data
public class UniversityMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UNIVERSITY_ID")
    private Long universityId;

    @Column(name = "UNIVERSITY_NAME")
    private String universityName;


    @Column(name = "STATUS")
    private boolean status;


}
