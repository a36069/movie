package com.abdi.movie.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OmdbRatingDTO {
    @JsonProperty("Source")
    private String source;
    @JsonProperty("Value")
    private String value;
}
