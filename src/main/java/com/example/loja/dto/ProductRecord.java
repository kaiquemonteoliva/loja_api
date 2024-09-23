package com.example.loja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public record ProductRecord<getValor>(
        @NotBlank String name,
        String description,
        @NotNull BigDecimal value,

        MultipartFile url_img


){

}
