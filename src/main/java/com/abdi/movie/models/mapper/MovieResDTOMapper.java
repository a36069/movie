package com.abdi.movie.models.mapper;

import com.abdi.movie.dtos.MovieResDto;
import com.abdi.movie.entities.MovieEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieResDTOMapper extends DTOEntityMapper<MovieEntity, MovieResDto> {
    default Page<MovieResDto> entityPageToDtoPage(Page<MovieEntity> entityPage) {
        List<MovieResDto> dtoList = toDto(entityPage.getContent());
        return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
    }
}
