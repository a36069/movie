package com.abdi.movie.models.mapper;

import com.abdi.movie.dtos.MovieWinnerResDto;
import com.abdi.movie.models.dtos.OmdbMovieDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OmdbMovieDTOMapper  {
    OmdbMovieDTO movieResDtotoOmdbMovieDTO(MovieWinnerResDto movieWinnerResDto);
    MovieWinnerResDto omdbMovieDTOtoMovieResDto(OmdbMovieDTO omdbMovieDTO);
}
