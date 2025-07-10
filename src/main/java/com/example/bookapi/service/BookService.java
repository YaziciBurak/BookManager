package com.example.bookapi.service;

import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.dto.BookResponseDto;
import com.example.bookapi.exception.BookNotFoundException;
import com.example.bookapi.entity.Book;
import com.example.bookapi.dto.BookMapper;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream().map(bookMapper::toDto).toList();
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return bookMapper.toDto(book);
    }

    public BookResponseDto createBook(BookRequestDto dto) {
        Book book = bookMapper.toEntity(dto);
        Book saved = bookRepository.save(book);
        return bookMapper.toDto(saved);
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book existing = bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
        bookMapper.updateEntity(existing, dto);
        Book updated = bookRepository.save(existing);
        return bookMapper.toDto(updated);
    }

}

