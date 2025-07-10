package com.example.bookapi.util;

import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.dto.BookResponseDto;

public class TestDataFactory {

    public static BookResponseDto createSampleBookResponse(Long id) {
         BookResponseDto dto = new BookResponseDto();
         dto.setId(id);
         dto.setTitle("Star Wars");
         dto.setAuthor("George Lucas");
         dto.setPublicationYear(1977);
         return dto;

    }

    public static BookRequestDto createSampleBookRequest() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Star Wars");
        dto.setAuthor("George Lucas");
        dto.setPublicationYear(1977);
        return dto;
    }
}
