package com.example.bookapi.service;

import com.example.bookapi.dto.BookResponseDto;
import com.example.bookapi.dto.BookRequestDto;
import com.example.bookapi.entity.Book;
import com.example.bookapi.exception.BookNotFoundException;
import com.example.bookapi.dto.BookMapper;
import com.example.bookapi.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;


    @Mock
    private BookRepository bookRepository;



    @Test
    void testCreateBook() {
        BookRequestDto requestDto = new BookRequestDto("Ningen Shikkaku", "Dazai Osamu", 1948);

        Book savedEntity = Book.builder()
                .id(1L)
                .title("Ningen Shikkaku")
                .author("Dazai Osamu")
                .publicationYear(1948)
                .build();

        when(bookMapper.toEntity(requestDto)).thenReturn(Book.builder()
                .title("Ningen Shikkaku")
                .author("Dazai Osamu")
                .publicationYear(1948)
                .build());

        when(bookRepository.save(any(Book.class))).thenReturn(savedEntity);

        when(bookMapper.toDto(savedEntity)).thenReturn(
                new BookResponseDto(1L, "Ningen Shikkaku", "Dazai Osamu", 1948)
        );

        BookResponseDto result = bookService.createBook(requestDto);

        assertNotNull(result);
        assertEquals("Ningen Shikkaku", result.getTitle());
        assertEquals("Dazai Osamu", result.getAuthor());
    }


    @Test
    void testGetAllBooks() {
        List<Book> books = List.of(
                new Book(1L, "Star Wars: A New Hope", "George Lucas", 1977),
                new Book(2L, "Yüzüklerin Efendisi: Yüzük Kardeşliği", "J.R.R. Tolkien", 1954)
        );

        when(bookRepository.findAll()).thenReturn(books);

        when(bookMapper.toDto(books.get(0))).thenReturn(new BookResponseDto(1L, "Star Wars: A New Hope", "George Lucas", 1977));
        when(bookMapper.toDto(books.get(1))).thenReturn(new BookResponseDto(2L, "Yüzüklerin Efendisi: Yüzük Kardeşliği", "J.R.R. Tolkien", 1954));

        List<BookResponseDto> result = bookService.getAllBooks();

        assertEquals("Star Wars: A New Hope", result.get(0).getTitle());
        assertEquals("Yüzüklerin Efendisi: Yüzük Kardeşliği", result.get(1).getTitle());
    }

    @Test
    void testUpdateBook_whenExists() {
        Long bookId = 1L;
        BookRequestDto updateDto = new BookRequestDto("Star Wars: Return of the Jedi", "Richard Marquand", 1983);

        Book existingBook = Book.builder()
                .id(bookId)
                .title("Star Wars: The Empire Strikes Back")
                .author("Irvin Kershner")
                .publicationYear(1980)
                .build();

        Book updatedBook = Book.builder()
                .id(bookId)
                .title(updateDto.getTitle())
                .author(updateDto.getAuthor())
                .publicationYear(updateDto.getPublicationYear())
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        when(bookMapper.toDto(updatedBook)).thenReturn(
                new BookResponseDto(bookId, updateDto.getTitle(), updateDto.getAuthor(), updateDto.getPublicationYear())
        );

        BookResponseDto result = bookService.updateBook(bookId, updateDto);

        assertEquals("Star Wars: Return of the Jedi", result.getTitle());
        assertEquals("Richard Marquand", result.getAuthor());
        assertEquals(1983, result.getPublicationYear());
    }

    @Test
    void testUpdateBook_whenNotFound() {
        Long bookId = 99L;
        BookRequestDto updateDto = new BookRequestDto("Nonexistent Book", "No Author", 2025);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(bookId, updateDto));
    }

    @Test
    void testDeleteBook_whenExists() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);

        assertDoesNotThrow(() -> bookService.deleteBook(bookId));
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    void testDeleteBook_whenNotFound() {
        Long bookId = 99L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
    }

    @Test
    void testGetBookById_whenExists() {
        Book book = Book.builder()
                .id(1L)
                .title("Yüzüklerin Efendisi: Yüzük Kardeşliği")
                .author("J.R.R. Tolkien")
                .publicationYear(1954)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        when(bookMapper.toDto(book)).thenReturn(
                new BookResponseDto(1L, "Yüzüklerin Efendisi: Yüzük Kardeşliği", "J.R.R. Tolkien", 1954)
        );

        BookResponseDto result = bookService.getBookById(1L);

        assertEquals("Yüzüklerin Efendisi: Yüzük Kardeşliği", result.getTitle());
    }

    @Test
    void testGetBookById_whenNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
    }
}
