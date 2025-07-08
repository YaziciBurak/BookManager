package com.example.bookapi.controller;

import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.dto.BookResponseDto;
import com.example.bookapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookResponseDto> getAll(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookResponseDto getById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PostMapping
    public BookResponseDto createBook(@RequestBody @Valid BookRequestDto requestDto){
        return bookService.createBook(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    public BookResponseDto  updateBook(@PathVariable Long id,@RequestBody @Valid BookRequestDto dto){
        return bookService.updateBook(id, dto);
    }
}
