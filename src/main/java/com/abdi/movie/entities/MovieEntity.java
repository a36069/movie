package com.abdi.movie.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String yearOscar;
    private String category;
    @Column(columnDefinition = "TEXT")
    private String nominee;
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;
    private Boolean won;

}