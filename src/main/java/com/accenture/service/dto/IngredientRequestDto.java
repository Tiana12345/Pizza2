package com.accenture.service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientRequestDto(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,
        @NotNull(message = "La quantité est obligatoire")
        Integer quantite
) {

}
