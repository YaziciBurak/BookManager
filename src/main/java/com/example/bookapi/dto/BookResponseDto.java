package com.example.bookapi.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private int publicationYear;
}
