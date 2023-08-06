package com.abdi.movie.services.datasource;

import com.abdi.movie.entities.MovieEntity;
import com.abdi.movie.exceptions.DataNotFoundException;
import com.abdi.movie.repositories.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Page<MovieEntity> findAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public MovieEntity findMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Movie not found"));
    }

    public long count() {
        return movieRepository.count();
    }

    public MovieEntity findAllByTitleAndWon(String title) {
        return movieRepository.findFirstByNomineeLikeOrAdditionalInfoLike(title, title).orElseThrow(() -> new DataNotFoundException("Movie not found"));
    }

    public MovieEntity save(MovieEntity entity) {
        return movieRepository.save(entity);
    }

    public List<MovieEntity> saveAll(List<MovieEntity> entity) {
        return movieRepository.saveAll(entity);
    }
}