package com.accenture.service.dto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IngredientRequestDto(
        @NotBlank(message ="Le nom de l'ingrédient doit être renseigné" )
        String nom,
        @NotNull(message = "La quantité de l'ingrédient doit être renseignée")
                @Min(value = 1, message = "La quantité doit être supérieure à zéro")
        Integer quantite
) {

}
