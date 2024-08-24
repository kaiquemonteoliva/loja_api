package com.example.loja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRecord(
        @NotBlank String name,
        String description,
        @NotNull BigDecimal value) {
}
