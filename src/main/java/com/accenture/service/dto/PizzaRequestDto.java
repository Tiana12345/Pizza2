package com.accenture.service.dto;

import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PizzaRequestDto(
        @NotBlank(message = "Le nom ne doit pas être vide")
        String nom,
        Map<Taille, Double> tarif,
        @NotNull(message = "Les ingrédients ne doivent pas être nuls")
        List<Integer> ingrs
) {
}