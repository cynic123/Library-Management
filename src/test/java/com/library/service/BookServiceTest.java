package com.library.service;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.model.Borrower;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

  private BookRepository bookRepository;
  private BorrowerService borrowerService;
  private BookService bookService;

  @BeforeEach
  void setUp() {
    bookRepository = mock(BookRepository.class);
    borrowerService = mock(BorrowerService.class);
    bookService = new BookService(bookRepository, borrowerService);
  }

  @Test
  void registerNewBook_Succeeds() {
    BookDTO bookDTO = new BookDTO("isbn1", "title1", "author1");
    when(bookRepository.findFirstByIsbn("isbn1")).thenReturn(Optional.empty());
    Book savedBook = Book.builder()
            .id(1L)
            .isbn("isbn1")
            .title("title1")
            .author("author1")
            .borrower(null)
            .build();
    when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

    Book result = bookService.register(bookDTO);

    assertEquals("isbn1", result.getIsbn());
    assertEquals("title1", result.getTitle());
    assertEquals("author1", result.getAuthor());
    assertNull(result.getBorrower());
  }

  @Test
  void registerBookWithDupIsbnButDifferentTitle_Throws() {
    BookDTO bookDTO = new BookDTO("isbn1", "A", "B");
    Book existing = Book.builder()
            .id(13L)
            .isbn("isbn1")
            .title("OtherTitle")
            .author("B")
            .borrower(null)
            .build();
    when(bookRepository.findFirstByIsbn("isbn1")).thenReturn(Optional.of(existing));

    assertThrows(IllegalArgumentException.class, () -> bookService.register(bookDTO));
  }

  @Test
  void borrowBook_Succeeds() {
    Borrower borrower = Borrower.builder()
            .id(23L)
            .name("BorrowerA")
            .email("a@b.com")
            .build();
    Book book = Book.builder()
            .id(33L)
            .isbn("isbn1")
            .title("title1")
            .author("author1")
            .borrower(null)
            .build();

    when(bookRepository.findById(33L)).thenReturn(Optional.of(book));
    when(borrowerService.getById(23L)).thenReturn(borrower);
    when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

    Book result = bookService.borrow(23L, 33L);
    assertEquals(borrower, result.getBorrower());
  }

  @Test
  void cannotBorrowAlreadyBorrowedBook_Throws() {
    Borrower borrower = Borrower.builder()
            .id(7L)
            .name("Borrowed")
            .email("b@b.com")
            .build();
    Book borrowedBook = Book.builder()
            .id(44L)
            .isbn("isbnborrowed")
            .title("titleB")
            .author("authorB")
            .borrower(borrower)
            .build();

    when(bookRepository.findById(44L)).thenReturn(Optional.of(borrowedBook));

    assertThrows(IllegalStateException.class, () -> bookService.borrow(9L, 44L));
  }
}