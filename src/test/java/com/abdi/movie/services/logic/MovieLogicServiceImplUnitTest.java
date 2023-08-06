package com.abdi.movie.services.logic;

import com.abdi.movie.dtos.MovieResDto;
import com.abdi.movie.dtos.MovieWinnerResDto;
import com.abdi.movie.entities.MovieEntity;
import com.abdi.movie.exceptions.MovieLogicException;
import com.abdi.movie.models.dtos.CsvMovieRecord;
import com.abdi.movie.models.dtos.OmdbMovieDTO;
import com.abdi.movie.models.mapper.MovieResDTOMapper;
import com.abdi.movie.models.mapper.OmdbMovieDTOMapper;
import com.abdi.movie.services.CsvService;
import com.abdi.movie.services.OmdbService;
import com.abdi.movie.services.datasource.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieLogicServiceImplUnitTest {

    @Mock
    private MovieService movieService;

    @Mock
    private CsvService csvService;
    @Mock
    private OmdbService omdbService;

    @InjectMocks
    private MovieLogicServiceImpl movieLogicService;
    @Captor
    private ArgumentCaptor<List<MovieEntity>> captor;
    @Mock
    private OmdbMovieDTOMapper omdbMovieDTOMapper;
    @Mock
    private MovieResDTOMapper movieResDTOMapper;

    @Test
    void givenForceSaveFalse_whenSaveMovies_thenSaveIsIgnored() throws Exception {
        // Given
        when(movieService.count()).thenReturn(1L);

        // When
        movieLogicService.saveMovies(false);

        // Then
        verify(movieService, times(1)).count();
        verify(movieService, never()).saveAll(anyList());
        verify(csvService, never()).readMoviesFromCsv();
    }

    @Test
    void givenForceSaveTrueAndMovieCountZero_whenSaveMovies_thenSaveIsDone() throws Exception {
        ReflectionTestUtils.setField(movieLogicService, "batchBaseSize", 2);

        // Given
        List<CsvMovieRecord> mockCsvMovieRecords = List.of(
                new CsvMovieRecord("2022", "category", "nominee", "additionalInfo", "YES"),
                new CsvMovieRecord("2022", "category", "nominee", "additionalInfo", "YES"),
                new CsvMovieRecord("2022", "category", "nominee", "additionalInfo", "YES"),
                new CsvMovieRecord("2021", "category", "nominee", "additionalInfo", "NO")
        );

        when(movieService.count()).thenReturn(0L);
        when(csvService.readMoviesFromCsv()).thenReturn(mockCsvMovieRecords);

        // When
        movieLogicService.saveMovies(true);

        // Then
        verify(movieService, times(1)).count();
        verify(csvService, times(1)).readMoviesFromCsv();

        verify(movieService, atLeastOnce()).saveAll(captor.capture());

        List<List<MovieEntity>> allValues = captor.getAllValues();

        assertEquals(2, allValues.size());
        List<MovieEntity> capturedMovies = allValues.get(0);
        assertEquals(2, capturedMovies.size());
        assertEquals("2022", capturedMovies.get(0).getYearOscar());
        assertEquals("category", capturedMovies.get(0).getCategory());
        assertEquals("nominee", capturedMovies.get(0).getNominee());
        assertEquals("additionalInfo", capturedMovies.get(0).getAdditionalInfo());
        assertTrue(capturedMovies.get(0).getWon());
    }

    @Test
    void givenReadMoviesThrowsException_whenSaveMovies_thenExceptionIsThrown() throws Exception {
        // Given
        when(movieService.count()).thenReturn(0L);
        when(csvService.readMoviesFromCsv()).thenThrow(new RuntimeException("CSV read failed"));

        // When
        Throwable thrown = catchThrowable(() -> movieLogicService.saveMovies(true));

        // Then
        assertThat(thrown).isInstanceOf(MovieLogicException.class);
        verify(movieService, times(1)).count();
        verify(movieService, never()).saveAll(anyList());
        verify(csvService, times(1)).readMoviesFromCsv();
    }

    @Test
    void givenTitle_whenGetBestPictureWinners_thenReturnsWinnerDetails() {
        //Given
        OmdbMovieDTO mockOmdbMovieDTO = new OmdbMovieDTO();
        mockOmdbMovieDTO.setResponse(true);
        when(omdbService.getMovieByTitle(anyString())).thenReturn(mockOmdbMovieDTO);

        MovieEntity mockMovieEntity = MovieEntity.builder()
                .yearOscar("2023")
                .category("Best Picture")
                .nominee("Movie Title")
                .additionalInfo("Additional Info")
                .won(true)
                .build();
        when(movieService.findAllByTitleAndWon(anyString())).thenReturn(mockMovieEntity);

        MovieWinnerResDto mockDto = new MovieWinnerResDto();
        when(omdbMovieDTOMapper.omdbMovieDTOtoMovieResDto(any(OmdbMovieDTO.class))).thenReturn(mockDto);
        String title = "Movie Title";

        //When
        MovieWinnerResDto result = movieLogicService.getBestPictureWinners(title);

        //Then
        verify(omdbService, times(1)).getMovieByTitle(anyString());
        verify(movieService, times(1)).findAllByTitleAndWon(anyString());
        verify(omdbMovieDTOMapper, times(1)).omdbMovieDTOtoMovieResDto(any(OmdbMovieDTO.class));

        assertNotNull(result);
        assertEquals("2023", result.getYearOscar());
        assertEquals("Best Picture", result.getCategory());
        assertEquals("Movie Title", result.getNominee());
        assertEquals("Additional Info", result.getAdditionalInfo());
        assertTrue(result.getWon());
    }




    @Test
    void givenMovieId_whenGetMovieById_thenReturnsMovie() {
        //Given
        MovieEntity mockMovieEntity = new MovieEntity();
        when(movieService.findMovieById(anyLong())).thenReturn(mockMovieEntity);

        MovieResDto mockDto = MovieResDto.builder().build();
        when(movieResDTOMapper.toDto(any(MovieEntity.class))).thenReturn(mockDto);
        Long id = 1L;

        //When
        MovieResDto result = movieLogicService.getMovieById(id);

        //Then
        verify(movieService, times(1)).findMovieById(id);
        verify(movieResDTOMapper, times(1)).toDto(any(MovieEntity.class));

        assertNotNull(result);
    }

    @Test
    void givenPageable_whenGetAllMovies_thenReturnsMoviePage() {
        //Given
        List<MovieEntity> movieEntities = List.of(new MovieEntity());
        Page<MovieEntity> movieEntityPage = new PageImpl<>(movieEntities);
        when(movieService.findAllMovies(any(Pageable.class))).thenReturn(movieEntityPage);

        Page<MovieResDto> movieDtoPage = new PageImpl<>(List.of(MovieResDto.builder().build()));
        when(movieResDTOMapper.entityPageToDtoPage(any(Page.class))).thenReturn(movieDtoPage);
        Pageable pageable = PageRequest.of(0, 5);

        //When
        Page<MovieResDto> result = movieLogicService.getAllMovies(pageable);

        //Then
        verify(movieService, times(1)).findAllMovies(pageable);
        verify(movieResDTOMapper, times(1)).entityPageToDtoPage(any(Page.class));

        assertNotNull(result);
    }
}