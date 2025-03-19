package com.accenture.service.serviceimpl;

import com.accenture.exception.IngredientException;
import com.accenture.repository.dao.IngredientDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.mapper.IngredientMapper;
import com.accenture.service.serviceimpl.IngredientServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @InjectMocks
    private IngredientServiceImpl service;
    @Mock
    private IngredientDao dao;
    @Mock
    private IngredientMapper mapper;

    @Test
    void testAjouterOK() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", 3);
        Ingredient ingrAvantEnreg = creerIngredient1();
        ingrAvantEnreg.setId(1);
        Ingredient ingrApresEnreg = creerIngredient1();
        IngredientResponseDto responseDto = creerIngredientResponseDto1();

        Mockito.when(mapper.toIngredient(ingredientRequestDto)).thenReturn(ingrAvantEnreg);
        Mockito.when(dao.save(ingrAvantEnreg)).thenReturn(ingrApresEnreg);
        Mockito.when(mapper.toIngredientResponseDto(ingrApresEnreg)).thenReturn(responseDto);
        assertSame(responseDto, service.ajouter(ingredientRequestDto));
        Mockito.verify(dao, Mockito.times(1)).save((ingrAvantEnreg));
    }

    @Test
    void testAjouterIngredientNull() {
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(null));
        Assertions.assertEquals("L'ingrédient doit exister", ex.getMessage());
    }

    @Test
    void testAjouterIngredientNomNull() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto(null, 3);
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(ingredientRequestDto));
        Assertions.assertEquals("Le nom de l'ingrédient doit être renseigné", ex.getMessage());
    }

    @Test
    void testAjouterIngredientNomBlank() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("", 3);
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(ingredientRequestDto));
        Assertions.assertEquals("Le nom de l'ingrédient doit être renseigné", ex.getMessage());
    }


    @Test
    void testAjouterIngredienQuantiteNull() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", null);
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(ingredientRequestDto));
        Assertions.assertEquals("La quantité de l'ingrédient doit être renseignée", ex.getMessage());
    }


    @Test
    void testAjouterIngredienQuantiteNegativeOuZero() {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", -1);
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(ingredientRequestDto));
        Assertions.assertEquals("La quantité doit être supérieur à zéro", ex.getMessage());
    }

    @Test
    void testListerIngredient() {
        Ingredient ingredient1 = creerIngredient1();
        Ingredient ingredient2 = creerIngredient2();
        List<Ingredient> liste = List.of(creerIngredient1(), creerIngredient2());
        IngredientResponseDto ingredientResponseDto1 = creerIngredientResponseDto1();
        IngredientResponseDto ingredientResponseDto2 = creerIngredientResponseDto2();
        List<IngredientResponseDto> dtos = List.of(ingredientResponseDto1, ingredientResponseDto2);

        Mockito.when(dao.findAll()).thenReturn(liste);
        Mockito.when(mapper.toIngredientResponseDto(ingredient1)).thenReturn(ingredientResponseDto1);
        Mockito.when(mapper.toIngredientResponseDto(ingredient2)).thenReturn(ingredientResponseDto2);
        assertEquals(dtos, service.trouverTous());
    }

    @Test
    void testModifierQuantite() {
        Ingredient ingredien1 = creerIngredient1();
        Optional<Ingredient> optIngr = Optional.of(ingredien1);
        Mockito.when(dao.findById(1)).thenReturn(optIngr);
        IngredientRequestDto ingredientRequestDto = creerIngredientRequestDto1();
        Mockito.when(mapper.toIngredient(ingredientRequestDto)).thenReturn(ingredien1);
        Ingredient ingrNouvQuant = new Ingredient(1, "Tomate", 2);
        Mockito.when(dao.save(ingredien1)).thenReturn(ingrNouvQuant);
        IngredientResponseDto ingredientResponseDto = creerIngredientResponseDto1();
        Mockito.when(mapper.toIngredientResponseDto(ingrNouvQuant)).thenReturn(ingredientResponseDto);
        assertEquals(ingredientResponseDto, service.modifierPartiellement(1, ingredientRequestDto));
    }




    @Test
    void testModifierQuantiteInferieurZero() {
        Ingredient ingredien1 = creerIngredient1();
        Optional<Ingredient> optIngr = Optional.of(ingredien1);
        Mockito.when(dao.findById(1)).thenReturn(optIngr);
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", -3);
        Mockito.when(mapper.toIngredient(ingredientRequestDto)).thenReturn(ingredien1);
        Ingredient ingrNouvQuant = new Ingredient(1, "Tomate", -3);
        Mockito.when(dao.save(ingredien1)).thenReturn(ingrNouvQuant);
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(1,"Tomate", -3);
        Mockito.when(mapper.toIngredientResponseDto(ingrNouvQuant)).thenReturn(ingredientResponseDto);
        assertEquals(ingredientResponseDto, service.modifierPartiellement(1, ingredientRequestDto));
    }

    @Test
    void testModifierNom() {
        Ingredient ingredien1 = creerIngredient1();
        Optional<Ingredient> optIngr = Optional.of(ingredien1);
        Mockito.when(dao.findById(1)).thenReturn(optIngr);
        IngredientRequestDto ingredientRequestDto = creerIngredientRequestDto1();
        Mockito.when(mapper.toIngredient(ingredientRequestDto)).thenReturn(ingredien1);
        Ingredient ingrNouvNom = new Ingredient(1, "Olive", 3);
        IngredientResponseDto ingredientResponseDto = creerIngredientResponseDto1();
        Mockito.when(dao.save(ingredien1)).thenReturn(ingrNouvNom);
        Mockito.when(mapper.toIngredientResponseDto(ingrNouvNom)).thenReturn(ingredientResponseDto);
        assertEquals(ingredientResponseDto, service.modifierPartiellement(1, ingredientRequestDto));
    }


    @Test
    void testModifierNomBlank() {
        Ingredient ingredien1 = creerIngredient1();
        Optional<Ingredient> optIngr = Optional.of(ingredien1);
        Mockito.when(dao.findById(1)).thenReturn(optIngr);
        IngredientRequestDto ingredientRequestDto = creerIngredientRequestDto1();
        Mockito.when(mapper.toIngredient(ingredientRequestDto)).thenReturn(ingredien1);
        Ingredient ingrNouvNom = new Ingredient(1, "", 3);
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(1,"", 3);
        Mockito.when(dao.save(ingredien1)).thenReturn(ingrNouvNom);
        Mockito.when(mapper.toIngredientResponseDto(ingrNouvNom)).thenReturn(ingredientResponseDto);
        assertEquals(ingredientResponseDto, service.modifierPartiellement(1, ingredientRequestDto));
    }


    @Test
    void testModifierIngrNull() {
        assertThrows(IngredientException.class, () -> service.modifierPartiellement(2, null));
    }

    @Test
    void testModifierIngrExistePas(){
        Mockito.when(dao.findById(2)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,()->service.modifierPartiellement(2, creerIngredientRequestDto1()));

    }




    private static Ingredient creerIngredient1 () {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setNom("Tomate");
        ingredient.setQuantite(3);
        return ingredient;
    }


    private static Ingredient creerIngredient2 () {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2);
        ingredient.setNom("Olive");
        ingredient.setQuantite(3);
        return ingredient;
    }

    private static IngredientResponseDto creerIngredientResponseDto1 () {
        return new IngredientResponseDto(1, "Tomate", 3);
    }
    private static IngredientResponseDto creerIngredientResponseDto2 () {
        return new IngredientResponseDto(2, "Olive", 3);
    }

    private static IngredientRequestDto creerIngredientRequestDto1 () {
        return new IngredientRequestDto("Tomate", 3);
    }
}
