package com.abdi.movie.services.logic;

import com.abdi.movie.dtos.MovieResDto;
import com.abdi.movie.dtos.MovieWinnerResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieLogicService {
    void saveMovies(boolean force);

    MovieResDto getMovieById(Long id);

    Page<MovieResDto> getAllMovies(Pageable pageable);

    MovieWinnerResDto getBestPictureWinners(String title);
}
