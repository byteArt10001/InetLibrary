package com.util;

import com.models.Book;
import com.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BookService bookService;

    @Autowired
    public BookValidator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (bookService.findDuplicate(book) != null){
            errors.rejectValue("name","","name is already exists");
            errors.rejectValue("author","","author is already exists");
            errors.rejectValue("yearEditions","","yearEditions is already exists");
        }
    }
}
