package com.abdi.movie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.function.Consumer;

@Configuration
public class GeneralWebClient {

    private final Integer timeout;
    private final Integer payloadSize;

    public GeneralWebClient(@Value("${httpClient.timeout:15}") Integer timeout,
                            @Value("${httpClient.payloadSize}") Integer payloadSize) {

        this.timeout = timeout;
        this.payloadSize = payloadSize;
    }

    public WebClient get(String baseUrl, Consumer<HttpHeaders> headersConsumer) {
        HttpClient httpClient =
                HttpClient.create().responseTimeout(Duration.ofSeconds(timeout));

        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(payloadSize)).build())
                .baseUrl(baseUrl)
                .defaultHeaders(headersConsumer)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
