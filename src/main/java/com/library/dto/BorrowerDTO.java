package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record BorrowerDTO(
        @NotBlank String name,
        @NotBlank @Email String email
) { }