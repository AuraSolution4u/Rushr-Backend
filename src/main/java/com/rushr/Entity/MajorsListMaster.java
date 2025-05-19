package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RH_MAJOR_LIST_MASTER")
@Data
public class MajorsListMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME_OF_MAJOR")
    private String nameOfMajor;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "STATUS")
    private boolean status;



}
