package com.accenture.service.service;

import com.accenture.exception.IngredientException;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface IngredientService {
    IngredientResponseDto ajouter(IngredientRequestDto ingredientRequestDto) throws IngredientException;
    List<IngredientResponseDto> trouverToutes();

    IngredientResponseDto modifierPartiellement(int id, IngredientRequestDto ingredientRequestDto) throws IngredientException, EntityNotFoundException;
}

