package com.accenture.service.serviceimpl;

import com.accenture.exception.IngredientException;
import com.accenture.repository.dao.IngredientDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.mapper.IngredientMapper;
import com.accenture.service.service.IngredientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientDao dao;
    private final IngredientMapper mapper;

    public IngredientServiceImpl(IngredientDao dao, IngredientMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    public IngredientResponseDto ajouter(IngredientRequestDto ingredientRequestDto) {
        verifierIndredientRequestDto(ingredientRequestDto);
        Ingredient ingredient = mapper.toIngredient(ingredientRequestDto);
        Ingredient ingredientSave = dao.save(ingredient);
        return mapper.toIngredientResponseDto(ingredient);
    }

    private static void verifierIndredientRequestDto(IngredientRequestDto ingredientRequestDto) {
        if (ingredientRequestDto == null)
            throw new IngredientException("L'ingrédient doit exister");
        if (ingredientRequestDto.nom() == null)
            throw new IngredientException("Le nom de l'ingrédient ne peut pas être nul");
        if (ingredientRequestDto.quantite() == null)
            throw new IngredientException("La quantité de l'ingrédient ne peut pas être nul");
    }

    @Override
    public List<IngredientResponseDto> trouverToutes() {
        return dao.findAll().stream()
                .map(mapper::toIngredientResponseDto)
                .toList();
    }
}
