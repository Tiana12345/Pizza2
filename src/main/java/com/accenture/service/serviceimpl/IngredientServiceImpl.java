package com.accenture.service.serviceimpl;

import com.accenture.exception.IngredientException;
import com.accenture.repository.dao.IngredientDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.mapper.IngredientMapper;
import com.accenture.service.service.IngredientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {

    public static final String ID_N_EXISTE_PAS = "L'id n'existe pas";
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


    @Override
    public List<IngredientResponseDto> trouverTous() {
        return dao.findAll().stream()
                .map(mapper::toIngredientResponseDto)
                .toList();
    }

    @Override
    public IngredientResponseDto modifierPartiellement(int id, IngredientRequestDto ingredientRequestDto) throws IngredientException, EntityNotFoundException {
        if (ingredientRequestDto == null)
            throw new IngredientException("L'ingredient ne peut pas être nul");
        Optional<Ingredient> optIngr = dao.findById(id);
        if (optIngr.isEmpty())
            throw new EntityNotFoundException(ID_N_EXISTE_PAS);
        Ingredient ingrExist = optIngr.get();
        Ingredient nouvIngr = mapper.toIngredient(ingredientRequestDto);
        remplacer(ingrExist, nouvIngr);
        Ingredient ingrEnreg = dao.save(ingrExist);
        return mapper.toIngredientResponseDto(ingrEnreg);
    }

    private static void verifierIndredientRequestDto(IngredientRequestDto ingredientRequestDto) {
        if (ingredientRequestDto == null)
            throw new IngredientException("L'ingrédient doit exister");
        if (ingredientRequestDto.nom() == null || ingredientRequestDto.nom().isBlank())
            throw new IngredientException("Le nom de l'ingrédient doit être renseigné");
        if (ingredientRequestDto.quantite() == null)
            throw new IngredientException("La quantité de l'ingrédient doit être renseignée");
        if (ingredientRequestDto.quantite() <= 0)
            throw new IngredientException("La quantité doit être supérieur à zéro");

    }
    private static void remplacer(Ingredient ingrExist, Ingredient nouvIngr) {
        if (nouvIngr.getNom() != null && !nouvIngr.getNom().isBlank())
            ingrExist.setNom(nouvIngr.getNom());
        if (nouvIngr.getQuantite() != null && nouvIngr.getQuantite() <= 0)
            ingrExist.setQuantite(nouvIngr.getQuantite());
    }
}

