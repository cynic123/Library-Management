package com.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Book {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank private String isbn;
  @NotBlank private String title;
  @NotBlank private String author;

  @ManyToOne
  @JoinColumn(name = "borrower_id")
  private Borrower borrower;
}