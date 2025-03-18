package com.accenture.service.service;

import com.accenture.exception.IngredientException;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;

import java.util.List;

public interface IngredientService {
    IngredientResponseDto ajouter(IngredientRequestDto ingredientRequestDto) throws IngredientException;
    List<IngredientResponseDto> trouverToutes();
}
