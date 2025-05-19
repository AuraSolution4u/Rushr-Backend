package com.rushr.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "RH_GREEK_ALPHABETS_MASTER")
@Data
public class GreekAlphabetsMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long greekAlphabetId;

    @Column(name = "GREEK_ALPHABET_NAME")
    private String greekAlphabetName;

    @Column(name = "DIRECTORY")
    private String directory;

}
