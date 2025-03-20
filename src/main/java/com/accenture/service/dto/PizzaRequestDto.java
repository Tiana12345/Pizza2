package com.accenture.service.dto;

import com.accenture.model.Taille;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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