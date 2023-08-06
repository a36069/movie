package com.abdi.movie.services.logic;

import com.abdi.movie.dtos.MovieResDto;
import com.abdi.movie.dtos.MovieWinnerResDto;
import com.abdi.movie.entities.MovieEntity;
import com.abdi.movie.exceptions.DataNotFoundException;
import com.abdi.movie.exceptions.MovieLogicException;
import com.abdi.movie.models.dtos.CsvMovieRecord;
import com.abdi.movie.models.dtos.OmdbMovieDTO;
import com.abdi.movie.models.mapper.MovieResDTOMapper;
import com.abdi.movie.models.mapper.OmdbMovieDTOMapper;
import com.abdi.movie.services.CsvService;
import com.abdi.movie.services.OmdbService;
import com.abdi.movie.services.datasource.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MovieLogicServiceImpl implements MovieLogicService {
    private final MovieService movieService;
    private final OmdbService omdbService;
    private final CsvService csvService;
    private final OmdbMovieDTOMapper omdbMovieDTOMapper;
    private final MovieResDTOMapper movieResDTOMapper;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchBaseSize;


    @Override
    public void saveMovies(boolean forceSave) {
        log.info("saveMovies Started with forceSave: {}", forceSave);

        if (movieService.count() > 0 && !forceSave) {
            log.info("saveMovies is ignore");
            return;
        }

        List<CsvMovieRecord> moviesFromCsv = null;
        try {
            moviesFromCsv = csvService.readMoviesFromCsv();
        } catch (Exception e) {
            throw new MovieLogicException(e);
        }

        List<MovieEntity> movieEntities = moviesFromCsv.stream()
                .filter(csvMovieRecord -> csvMovieRecord.getWon().equals("YES"))
                .map(csvMovieRecord -> MovieEntity.builder()
                        .yearOscar(csvMovieRecord.getYear())
                        .category(csvMovieRecord.getCategory())
                        .nominee(csvMovieRecord.getNominee())
                        .additionalInfo(csvMovieRecord.getAdditionalInfo())
                        .won(true)
                        .build())
                .toList();

        for (int i = 0; i < movieEntities.size(); i += batchBaseSize) {
            List<MovieEntity> batch = movieEntities.subList(i, Math.min(i + batchBaseSize, movieEntities.size()));
            movieService.saveAll(batch);
        }

        log.info("saveMovies is Done with this size items {} ", movieEntities.size());
    }

    @Override
    public MovieResDto getMovieById(Long id) {
        log.info("getMovieById Started with Movie Id {}  ", id);
        MovieResDto movieDTO = movieResDTOMapper.toDto(movieService.findMovieById(id));
        log.info("getMovieById is Done with this data {}", movieDTO);
        return movieDTO;
    }

    @Override
    public Page<MovieResDto> getAllMovies(Pageable pageable) {
        log.info("getAllMovies Started with this pageable {}  ", pageable);
        Page<MovieResDto> movieDTOs = movieResDTOMapper.entityPageToDtoPage(movieService.findAllMovies(pageable));
        log.info("getAllMovies is Done with this data {}", movieDTOs);
        return movieDTOs;
    }

    @Cacheable(value = "bestPictureWinners", key = "#title")
    @Override
    public MovieWinnerResDto getBestPictureWinners(String title) {
        log.info("getBestPictureWinners Started with this title {} ", title);
        OmdbMovieDTO omdbMovieDTO = omdbService.getMovieByTitle(title.replaceAll("[\"{}]", ""));

        if (!omdbMovieDTO.isResponse())
            throw new DataNotFoundException("Movie not found");

        MovieWinnerResDto responseDto = omdbMovieDTOMapper.omdbMovieDTOtoMovieResDto(omdbMovieDTO);
        MovieEntity movieEntity = movieService.findAllByTitleAndWon(title);
        responseDto = responseDto.toBuilder()
                .yearOscar(movieEntity.getYearOscar())
                .category(movieEntity.getCategory())
                .nominee(movieEntity.getNominee())
                .additionalInfo(movieEntity.getAdditionalInfo())
                .won(movieEntity.getWon())
                .build();
        log.info("getBestPictureWinners is Done with this data {}", responseDto);
        return responseDto;
    }
}
