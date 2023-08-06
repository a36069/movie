package com.abdi.movie.services;

import com.abdi.movie.models.dtos.CsvMovieRecord;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.List;

@Slf4j
@Service
public class CsvService {
    @Value("classpath:static/awards.csv")
    Resource resource;

    public List<CsvMovieRecord> readMoviesFromCsv() throws Exception {
        log.info("readMoviesFromCsv Started");

        try (Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            CsvToBean<CsvMovieRecord> csvToBean = new CsvToBeanBuilder<CsvMovieRecord>(reader)
                    .withType(CsvMovieRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            var csvRecords = csvToBean.parse();

            log.info("getMovieByTitle is Done with this size {}", csvRecords.size());
            return csvRecords;
        }
    }
}
