package com.library.dto;

import jakarta.validation.constraints.NotBlank;

public record BookDTO(
        @NotBlank String isbn,
        @NotBlank String title,
        @NotBlank String author
) { }