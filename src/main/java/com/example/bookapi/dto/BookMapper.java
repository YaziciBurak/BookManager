package com.example.bookapi.dto;

import com.example.bookapi.entity.Book;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookResponseDto toDto(Book book);

    Book toEntity(BookRequestDto dto);

    void updateEntity(@MappingTarget Book entity, BookRequestDto dto);
}
