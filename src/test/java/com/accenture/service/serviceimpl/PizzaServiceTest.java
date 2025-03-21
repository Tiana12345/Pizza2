package com.accenture.service.serviceimpl;

import com.accenture.exception.PizzaException;
import com.accenture.model.Taille;
import com.accenture.repository.dao.IngredientDao;
import com.accenture.repository.dao.PizzaDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PizzaServiceTest {
    @InjectMocks
    private PizzaServiceImpl service;
    @Mock
    PizzaDao pizzaDao = Mockito.mock(PizzaDao.class);
    @Mock
    IngredientDao ingredientDao = Mockito.mock(IngredientDao.class);


    @Test
    void ajouterUnePizzaOk() throws PizzaException {
        List<Ingredient> ingredients = List.of(
                new Ingredient("Tomate", 5),
                new Ingredient("Fromage", 7),
                new Ingredient("Jambon", 3)
        );
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 9.0);
        tarifMap.put(Taille.MOYENNE, 13.0);
        tarifMap.put(Taille.GRANDE, 17.0);

        PizzaRequestDto requestDto = new PizzaRequestDto("Reine", tarifMap, List.of(1, 2, 3));

        Pizza pizzaEnreg = new Pizza("Reine", tarifMap, ingredients);
        pizzaEnreg.setId(1);
        PizzaResponseDto responseDto = new PizzaResponseDto(1, "Reine", tarifMap, List.of("Tomate", "Fromage", "Jambon"));

        when(ingredientDao.findAllById(requestDto.ingrs())).thenReturn(ingredients);
        when(pizzaDao.save(any(Pizza.class))).thenReturn(pizzaEnreg);

        assertEquals(responseDto, service.ajouter(requestDto));
        verify(pizzaDao, Mockito.times(1)).save(any(Pizza.class));
        verify(ingredientDao, Mockito.times(1)).findAllById(requestDto.ingrs());
    }

    @Test
    void ajouterPizzaIngrNul() {
        Map<Taille, Double> tarifMap = getTarifMap();

        PizzaRequestDto dto = new PizzaRequestDto("norvegienne", tarifMap, null);
        assertThrows(PizzaException.class, () -> service.ajouter(dto));
    }

    @Test
    void ajouterPizzaTarif0() {
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.GRANDE, 0.0);

        PizzaRequestDto dto = new PizzaRequestDto("norvegienne", tarifMap, List.of(1, 2, 3));
        assertThrows(PizzaException.class, () -> service.ajouter(dto));
    }
//__________________________________________________________________________
@Test
void testModifierPartiellementOk() throws PizzaException, EntityNotFoundException {
    // Préparer les données de test
    int id = 1;
    Pizza pizzaExistant = margherita();
    Map<Taille, Double> tarifMap = new HashMap<>();
    tarifMap.put(Taille.PETITE, 9.0);
    tarifMap.put(Taille.MOYENNE, 13.0);
    tarifMap.put(Taille.GRANDE, 17.0);
    PizzaRequestDto requestDto = new PizzaRequestDto("Reine", tarifMap, List.of(1, 2, 3));
    List<Ingredient> ingredients = List.of(
            new Ingredient("Tomate", 5),
            new Ingredient("Fromage", 7),
            new Ingredient("Jambon", 3)
    );
    Pizza nouvelle = new Pizza("Reine", tarifMap, ingredients);
    PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1, "Reine", tarifMap, List.of("Tomate", "Fromage", "Jambon"));

    // Simuler les appels de méthodes
    when(pizzaDao.findById(id)).thenReturn(Optional.of(pizzaExistant));
    when(ingredientDao.findAllById(requestDto.ingrs())).thenReturn(ingredients);
    when(pizzaDao.save(any(Pizza.class))).thenReturn(pizzaExistant);

    // Appeler la méthode à tester
    PizzaResponseDto result = service.modifierPartiellement(id, requestDto);

    // Vérifier les résultats
    assertNotNull(result);
    assertEquals(pizzaResponseDto, result);

    // Vérifier que les méthodes simulées ont été appelées
    verify(pizzaDao).findById(id);
    verify(ingredientDao).findAllById(requestDto.ingrs());
    verify(pizzaDao).save(any(Pizza.class));
}

    @DisplayName("Test modifier partiellement une pizza / pizza non trouvée")
    @Test
    void testModifierPartiellementPizzaNonTrouve() {
        int id = 10;
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 9.0);
        tarifMap.put(Taille.MOYENNE, 13.0);
        tarifMap.put(Taille.GRANDE, 17.0);
        PizzaRequestDto requestDto = new PizzaRequestDto("Reine", tarifMap, List.of(1, 2, 3));

        when(pizzaDao.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.modifierPartiellement(id, requestDto));

        verify(pizzaDao).findById(id);
        verify(pizzaDao, never()).save(any());
    }

    @Test
    void trouverToutes() {
        Pizza margherita = margherita();
        Pizza reine = reine();

        List<Pizza> pizzas = List.of(margherita, reine);
        List<PizzaResponseDto> dtos = List.of(pizzaResponseDto1(), pizzaResponseDto2());

        when(pizzaDao.findAll()).thenReturn(pizzas);

        assertEquals(dtos, service.trouverTous());
    }

    @DisplayName("Test rechercher pizza")
    @Test
    void testRechercherPizza() {
        Pizza margherita = margherita();
        Pizza reine = reine();
        List<Pizza> pizzas = Arrays.asList(margherita, reine);

        PizzaResponseDto pizzaResponseDto1 = pizzaResponseDto1();
        PizzaResponseDto pizzaResponseDto2 = pizzaResponseDto2();

        when(pizzaDao.findAll()).thenReturn(pizzas);

        List<PizzaResponseDto> result = service.rechercher(null, "Margherita", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Margherita", result.get(0).nom());

        verify(pizzaDao).findAll();
    }

//    ___________________________________________________________
//            METHODES PRIVEES
//    ___________________________________________________________
private static List<Ingredient> getIngredientList() {
    List<Ingredient> ingredients = List.of(new Ingredient("Tomate", 5), new Ingredient("Fromage", 7), new Ingredient("Jambon", 3));
    return ingredients;
}

    private static Map<Taille, Double> getTarifMap() {
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 9.0);
        tarifMap.put(Taille.MOYENNE, 13.0);
        tarifMap.put(Taille.GRANDE, 17.0);
        return tarifMap;
    }
    public Pizza margherita() {
        List<Ingredient> ingredients = List.of(
                new Ingredient("Tomate", 5),
                new Ingredient("Mozzarella", 7),
                new Ingredient("Basilic", 2)
        );
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 8.0);
        tarifMap.put(Taille.MOYENNE, 12.0);
        tarifMap.put(Taille.GRANDE, 16.0);

        Pizza pizza = new Pizza("Margherita", tarifMap, ingredients);
        pizza.setId(1);
        return pizza;
    }
    public Pizza reine() {
        List<Ingredient> ingredients = List.of(
                new Ingredient("Tomate", 5),
                new Ingredient("Fromage", 7),
                new Ingredient("Jambon", 3),
                new Ingredient("Champignons", 4)
        );
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 9.0);
        tarifMap.put(Taille.MOYENNE, 13.0);
        tarifMap.put(Taille.GRANDE, 17.0);

        Pizza pizza = new Pizza("Reine", tarifMap, ingredients);
        pizza.setId(2);
        return pizza;
    }
    public PizzaResponseDto pizzaResponseDto1() {
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 8.0);
        tarifMap.put(Taille.MOYENNE, 12.0);
        tarifMap.put(Taille.GRANDE, 16.0);

        List<String> ingredients = List.of("Tomate", "Mozzarella", "Basilic");

        return new PizzaResponseDto(1, "Margherita", tarifMap, ingredients);
    }
    public PizzaResponseDto pizzaResponseDto2() {
        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 9.0);
        tarifMap.put(Taille.MOYENNE, 13.0);
        tarifMap.put(Taille.GRANDE, 17.0);

        List<String> ingredients = List.of("Tomate", "Fromage", "Jambon", "Champignons");

        return new PizzaResponseDto(2, "Reine", tarifMap, ingredients);
    }
}
