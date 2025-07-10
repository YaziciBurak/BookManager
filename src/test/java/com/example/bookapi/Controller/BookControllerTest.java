package com.example.bookapi.Controller;

import com.example.bookapi.controller.BookController;
import com.example.bookapi.dto.BookResponseDto;
import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.example.bookapi.util.TestDataFactory.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void shouldGetAllBooks() throws Exception {
        List<BookResponseDto> books = List.of(createSampleBookResponse(1L));

        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Star Wars"));
    }

    @Test
    void shouldGetBookById() throws Exception {
        BookResponseDto book = createSampleBookResponse(1L);

        Mockito.when(bookService.getBookById(1L)).thenReturn(book);

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Star Wars"));
    }

    @Test
    void shouldCreateBook() throws Exception {
        BookRequestDto request = createSampleBookRequest();
        BookResponseDto response = createSampleBookResponse(1L);

        Mockito.when(bookService.createBook(any(BookRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Star Wars"));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookRequestDto request = createSampleBookRequest();
        BookResponseDto updated = createSampleBookResponse(1L);

        Mockito.when(bookService.updateBook(Mockito.eq(1L), any(BookRequestDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Star Wars"));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        Mockito.doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }
}
