package com.example.bookapi.controller;

import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.dto.BookResponseDto;
import com.example.bookapi.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(description = "Lists all book records in the database.")
    public List<BookResponseDto> getAll(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @Operation(description = "Retrieves the book with the specified ID.")
    public BookResponseDto getById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(description = "Creates a new book record with the provided information.")
    public BookResponseDto createBook(@RequestBody @Valid BookRequestDto requestDto){
        return bookService.createBook(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(description = "Deletes the book with the specified ID.")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    @Operation(description = "Updates the information of the book with the specified ID.")
    public BookResponseDto  updateBook(@PathVariable Long id,@RequestBody @Valid BookRequestDto dto){
        return bookService.updateBook(id, dto);
    }
}
