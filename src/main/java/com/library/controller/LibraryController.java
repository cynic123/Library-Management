package com.library.controller;

import com.library.dto.BookDTO;
import com.library.dto.BorrowerDTO;
import com.library.model.Book;
import com.library.model.Borrower;
import com.library.service.BookService;
import com.library.service.BorrowerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {
  private final BorrowerService borrowerService;
  private final BookService bookService;

  public LibraryController(BorrowerService borrowerService, BookService bookService) {
    this.borrowerService = borrowerService;
    this.bookService = bookService;
  }

  @PostMapping("/borrowers")
  public ResponseEntity<Borrower> registerBorrower(@RequestBody @Valid BorrowerDTO dto) {
    return ResponseEntity.ok(borrowerService.register(dto));
  }

  @GetMapping("/borrowers")
  public List<Borrower> listBorrowers() {
    return borrowerService.listAll();
  }

  @PostMapping("/books")
  public ResponseEntity<Book> registerBook(@RequestBody @Valid BookDTO dto) {
    return ResponseEntity.ok(bookService.register(dto));
  }

  @GetMapping("/books")
  public List<Book> listBooks() {
    return bookService.listAll();
  }

  @PostMapping("/borrow/{borrowerId}/book/{bookId}")
  public ResponseEntity<Book> borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
    return ResponseEntity.ok(bookService.borrow(borrowerId, bookId));
  }

  @PostMapping("/return/{borrowerId}/book/{bookId}")
  public ResponseEntity<Book> returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
    return ResponseEntity.ok(bookService.returnBook(borrowerId, bookId));
  }
}