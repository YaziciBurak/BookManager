package com.example.bookapi.service;

import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.dto.BookResponseDto;
import com.example.bookapi.exception.BookNotFoundException;
import com.example.bookapi.model.Book;
import com.example.bookapi.dto.BookMapper;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream().map(BookMapper::toDto).toList();
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return BookMapper.toDto(book);
    }

    public BookResponseDto createBook(BookRequestDto dto) {
        Book book = BookMapper.toEntity(dto);
        Book saved = bookRepository.save(book);
        return BookMapper.toDto(saved);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book existing = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
        BookMapper.updateEntity(existing, dto);
        Book updated = bookRepository.save(existing);
        return BookMapper.toDto(updated);
    }

}

