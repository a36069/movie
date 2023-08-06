package com.abdi.movie.models.dtos;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CsvMovieRecord {
    @CsvBindByName(column = "Year")
    private String year;

    @CsvBindByName(column = "Category")
    private String category;

    @CsvBindByName(column = "Nominee")
    private String nominee;

    @CsvBindByName(column = "Additional Info")
    private String additionalInfo;

    @CsvBindByName(column = "Won?")
    private String won;
}
