package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.BookDTO;
import com.library.dto.BorrowerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LibraryControllerIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  void addAndGetBook() throws Exception {
    BookDTO bookDTO = new BookDTO("12345", "Test Book", "Author A");
    mockMvc.perform(post("/library/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bookDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isbn").value("12345"))
            .andExpect(jsonPath("$.title").value("Test Book"));

    // For robustness, check for any book with isbn="12345"
    mockMvc.perform(get("/library/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[?(@.isbn == '12345')]").exists());
  }

  @Test
  void addBorrower() throws Exception {
    BorrowerDTO borrowerDTO = new BorrowerDTO("User1", "user1@email.com");
    mockMvc.perform(post("/library/borrowers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(borrowerDTO)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("user1@email.com"));
  }

  @Test
  void completeBorrowAndReturnCycle() throws Exception {
    // Register borrower
    BorrowerDTO borrowerDTO = new BorrowerDTO("User2", "user2@email.com");
    String borrowerJson = objectMapper.writeValueAsString(borrowerDTO);
    String borrowerResponse = mockMvc.perform(post("/library/borrowers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(borrowerJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    Long borrowerId = objectMapper.readTree(borrowerResponse).get("id").asLong();

    // Register book
    BookDTO bookDTO = new BookDTO("56789", "Borrow Test Book", "Author B");
    String bookJson = objectMapper.writeValueAsString(bookDTO);
    String bookApiResp = mockMvc.perform(post("/library/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
    Long bookId = objectMapper.readTree(bookApiResp).get("id").asLong();

    // Borrow the book
    mockMvc.perform(post("/library/borrow/{borrowerId}/book/{bookId}", borrowerId, bookId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.borrower.id").value(borrowerId));

    // Return the book
    mockMvc.perform(post("/library/return/{borrowerId}/book/{bookId}", borrowerId, bookId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.borrower").doesNotExist());
  }
}