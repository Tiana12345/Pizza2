package com.accenture.service.dto;

import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import jakarta.validation.constraints.NotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PizzaResponseDto(
        int id,
        String nom,
        Map<Taille,Double> tarif,
        List<String> ingredients) {
}
