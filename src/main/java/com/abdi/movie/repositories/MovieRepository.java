package com.abdi.movie.repositories;

import com.abdi.movie.entities.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    Optional<MovieEntity> findFirstByNomineeLikeOrAdditionalInfoLike(String nominee, String additionalInfo);

}