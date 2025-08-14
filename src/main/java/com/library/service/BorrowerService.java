package com.library.service;

import com.library.dto.BorrowerDTO;
import com.library.model.Borrower;
import com.library.repository.BorrowerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowerService {
  private static final Logger log = LoggerFactory.getLogger(BorrowerService.class);
  private final BorrowerRepository borrowerRepository;

  public BorrowerService(BorrowerRepository borrowerRepository) {
    this.borrowerRepository = borrowerRepository;
  }

  public Borrower register(BorrowerDTO dto) {
    if (borrowerRepository.findByEmail(dto.email()).isPresent()) {
      log.error("Error while registering book, book: {}", dto);
      throw new IllegalArgumentException("Email is already registered");
    }

    return borrowerRepository.save(Borrower.builder()
            .name(dto.name())
            .email(dto.email())
            .build());
  }

  public List<Borrower> listAll() {
    return borrowerRepository.findAll();
  }

  public Borrower getById(Long id) {
    return borrowerRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Borrower not found"));
  }
}