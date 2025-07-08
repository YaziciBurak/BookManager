package com.example.bookapi.dto;

import com.example.bookapi.model.Book;

public class BookMapper {

    public static BookResponseDto toDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publicationYear(book.getPublicationYear())
                .build();
    }

    public static Book toEntity(BookRequestDto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publicationYear(dto.getPublicationYear())
                .build();
    }

    public static void updateEntity(Book entity, BookRequestDto dto) {
        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setPublicationYear(dto.getPublicationYear());
    }
}
