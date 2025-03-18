package com.accenture.service.dto;

import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;

import java.util.List;

public record PizzaResponseDto(
        String nom,
        Taille taille,
        Double tarif,
        List<Ingredient> ingredients) {
}
