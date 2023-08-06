package com.abdi.movie.jobs;

import com.abdi.movie.services.logic.MovieLogicServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MovieRunner implements ApplicationRunner {
    private final MovieLogicServiceImpl movieLogicServiceImpl;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("saveMoviesStartup Started");
        movieLogicServiceImpl.saveMovies(false);
        log.info("saveMoviesStartup is Done");
    }
}
