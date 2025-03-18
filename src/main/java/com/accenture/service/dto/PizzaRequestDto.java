package com.accenture.service.dto;

import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PizzaRequestDto(
        @NotBlank
        String nom,
        @NotNull
        Taille taille,
        @NotNull
        Double tarif,
        @NotNull
        List<Ingredient> ingredients


) {
}
