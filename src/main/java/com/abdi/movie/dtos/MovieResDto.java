package com.abdi.movie.dtos;

import com.abdi.movie.entities.MovieEntity;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link MovieEntity}
 */
@Builder
public record MovieResDto(Long id, String yearOscar, String category, String nominee, String additionalInfo,
                          Boolean won) implements Serializable {
}