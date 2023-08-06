package com.abdi.movie.controllers;

import com.abdi.movie.dtos.MovieResDto;
import com.abdi.movie.dtos.MovieWinnerResDto;
import com.abdi.movie.dtos.Response;
import com.abdi.movie.services.MessageComponent;
import com.abdi.movie.services.logic.MovieLogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieLogicService movieLogicService;
    private final MessageComponent messageComponent;


    @GetMapping
    public Response<Page<MovieResDto>> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "50") int size) {
        return Response.<Page<MovieResDto>>builder()
                .body(movieLogicService.getAllMovies(PageRequest.of(page, size)))
                .code(messageComponent.getSuccessCode())
                .message(messageComponent.getSuccessMessage())
                .build();
    }

    @GetMapping("/{id}")
    public Response<MovieResDto> getMovieById(@PathVariable Long id) {
        return Response.<MovieResDto>builder()
                .body(movieLogicService.getMovieById(id))
                .code(messageComponent.getSuccessCode())
                .message(messageComponent.getSuccessMessage())
                .build();
    }

    @GetMapping("/best-picture-winners/{title}")
    public Response<MovieWinnerResDto> getBestPictureWinners(@PathVariable String title) {
        return Response.<MovieWinnerResDto>builder()
                .body(movieLogicService.getBestPictureWinners(title))
                .code(messageComponent.getSuccessCode())
                .message(messageComponent.getSuccessMessage())
                .build();
    }
}
