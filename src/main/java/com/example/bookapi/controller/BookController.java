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
    @Operation(description = "Veritabanındaki tüm kitap kayıtlarını listeler.")
    public List<BookResponseDto> getAll(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    @Operation(description = "Verilen ID'ye sahip kitabı getirir.")
    public BookResponseDto getById(@PathVariable Long id){
        return bookService.getBookById(id);
    }

    @PostMapping
    @Operation(description = "Verilen bilgileri kullanarak yeni bir kitap kaydı oluşturur.")
    public BookResponseDto createBook(@RequestBody @Valid BookRequestDto requestDto){
        return bookService.createBook(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(description = "Verilen ID'ye sahip kitabı siler.")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @PutMapping("/{id}")
    @Operation(description = "Verilen ID'ye sahip kitabın bilgilerini günceller.")
    public BookResponseDto  updateBook(@PathVariable Long id,@RequestBody @Valid BookRequestDto dto){
        return bookService.updateBook(id, dto);
    }
}
