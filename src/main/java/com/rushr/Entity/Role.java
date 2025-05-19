package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RH_ROLE")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "CHAPTER_ID")
    private Chapter chapterId;


}
