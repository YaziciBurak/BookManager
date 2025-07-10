package com.example.bookapi.entity;

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
