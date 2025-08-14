package com.library.service;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.model.Borrower;
import com.library.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
  private static final Logger log = LoggerFactory.getLogger(BookService.class);
  private final BookRepository bookRepository;
  private final BorrowerService borrowerService;

  public BookService(BookRepository bookRepository, BorrowerService borrowerService) {
    this.bookRepository = bookRepository;
    this.borrowerService = borrowerService;
  }

  public Book register(BookDTO dto) {
    bookRepository.findFirstByIsbn(dto.isbn()).ifPresent(existing -> {
      if (!existing.getTitle().equals(dto.title()) || !existing.getAuthor().equals(dto.author())) {
        log.error("Error while registering book, title: {}, author: {}", dto.title(), dto.author());
        throw new IllegalArgumentException("Different title/author with same ISBN exists");
      }
    });
    return bookRepository.save(Book.builder()
            .isbn(dto.isbn())
            .title(dto.title())
            .author(dto.author())
            .borrower(null)
            .build());
  }

  public List<Book> listAll() {
    return bookRepository.findAll();
  }

  public Book getById(Long id) {
    return bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
  }

  public Book borrow(Long borrowerId, Long bookId) {
    Borrower borrower = borrowerService.getById(borrowerId);
    Book book = getById(bookId);
    if (book.getBorrower() != null)
      throw new IllegalStateException("Book already borrowed");
    book.setBorrower(borrower);
    return bookRepository.save(book);
  }

  public Book returnBook(Long borrowerId, Long bookId) {
    Book book = getById(bookId);
    if (book.getBorrower() == null || !book.getBorrower().getId().equals(borrowerId))
      throw new IllegalStateException("This borrower does not have this book");
    book.setBorrower(null);
    return bookRepository.save(book);
  }
}