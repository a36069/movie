package com.abdi.movie.models.mapper;

import org.mapstruct.MappingTarget;
import java.util.List;

public interface DTOEntityMapper<E, D> {
    E toEntity(D dto);

    E dtoToEntity(@MappingTarget E entity , D dto);

    List<E> toEntity(List<D> dto);

    D toDto(E entity);

    List<D> toDto(List<E> entity);



}
