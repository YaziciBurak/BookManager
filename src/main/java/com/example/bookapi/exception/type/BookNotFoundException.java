package com.example.bookapi.exception.type;

import com.example.bookapi.constants.ErrorMessages;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(Long id) {
        super(String.format(ErrorMessages.BOOK_NOT_FOUND, id));
    }
}
