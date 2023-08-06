package com.abdi.movie.services;

import com.abdi.movie.config.GeneralWebClient;
import com.abdi.movie.models.dtos.OmdbMovieDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OmdbService {
    private final WebClient webClient;
    @Value("${third_api.omdb.apikey}")
    private String apiKey;

    public OmdbService(GeneralWebClient generalWebClient,
                       @Value("${third_api.omdb.base_url}") String baseUrl) {
        this.webClient = generalWebClient.get(baseUrl,
                headers -> headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    public OmdbMovieDTO getMovieByTitle(String title) {
        log.info("getMovieByTitle Started with this data {} ", title);

        OmdbMovieDTO omdbRes = this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/")
                        .queryParam("t", title)
                        .queryParam("apiKey", apiKey)
                        .build())
                //TODO we can handel status
                .retrieve()
                .bodyToMono(OmdbMovieDTO.class)
                .block();

        log.info("getMovieByTitle is Done with this data {}", omdbRes);
        return omdbRes;
    }
}
