package com.example.bookapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;

private String title;
private String author;
private int publicationYear;
}
