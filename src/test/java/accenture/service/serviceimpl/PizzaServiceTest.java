package accenture.service.serviceimpl;

import com.accenture.exception.PizzaException;
import com.accenture.model.Taille;
import com.accenture.repository.dao.PizzaDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.service.mapper.PizzaMapper;
import com.accenture.service.serviceimpl.PizzaServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PizzaServiceTest {
    @InjectMocks
    private PizzaServiceImpl service;
    @Mock
    PizzaDao dao = Mockito.mock(PizzaDao.class);
    @Mock
    PizzaMapper mapperMock;


    @Test
    void ajouterUnePizzaOk() {
        Pizza pizzaAvantEnreg = reine();
        pizzaAvantEnreg.setId(1);
        PizzaRequestDto requestdto = pizzaRequestDto2();

        Pizza clientApresEnreg = reine();
        PizzaResponseDto responseDto = pizzaResponseDto2();

        when(mapperMock.toPizza(requestdto)).thenReturn(pizzaAvantEnreg);
        when(dao.save(pizzaAvantEnreg)).thenReturn(clientApresEnreg);
        when(mapperMock.toPizzaResponseDto(clientApresEnreg)).thenReturn(responseDto);

        assertSame(responseDto, service.ajouter(requestdto));
        verify(dao, Mockito.times(1)).save(pizzaAvantEnreg);
    }


    @Test
    void ajouterUnePizzaNomNull() {
        PizzaRequestDto dto = new PizzaRequestDto(1, null, Taille.GRANDE, 15.0, listIingredients());
        assertThrows(PizzaException.class, ()-> service.ajouter(dto));
    }

    @Test
    void ajouterUnePizzaNomBlank() {
        PizzaRequestDto dto = new PizzaRequestDto(1, "  \t", Taille.GRANDE, 15.0, listIingredients());
        assertThrows(PizzaException.class, ()-> service.ajouter(dto));
    }

    @Test
    void ajouterPizzaTailleNulle() {
        PizzaRequestDto dto = new PizzaRequestDto(1, "norvegienne", null, 15.0, listIingredients());
        assertThrows(PizzaException.class, ()-> service.ajouter(dto));
    }


    @Test
    void ajouterPizzaTarifNulle() {
        PizzaRequestDto dto = new PizzaRequestDto(1, "norvegienne", Taille.GRANDE, null, listIingredients());
        assertThrows(PizzaException.class, ()-> service.ajouter(dto));
    }

    @Test
    void ajouterPizzaTarisInf0() {
        PizzaRequestDto dto = new PizzaRequestDto(1, "norvegienne", Taille.GRANDE, -3.0, listIingredients());
        assertThrows(PizzaException.class, ()-> service.ajouter(dto));
    }
//__________________________________________________________________________

    @Test
    void testModifierPartiellementOk() throws PizzaException, EntityNotFoundException {
        // Préparer les données de test
        int id = 1;
        Pizza pizzaExistant = margherita();
        pizzaRequestDto1();
        Pizza nouvelle = reine();
        PizzaResponseDto pizzaResponseDto = pizzaResponseDto2();

        // Simuler les appels de méthodes
        when(dao.findById(id)).thenReturn(Optional.of(pizzaExistant));
        when(mapperMock.toPizza(pizzaRequestDto1())).thenReturn(nouvelle);
        when(dao.save(pizzaExistant)).thenReturn(pizzaExistant);
        when(mapperMock.toPizzaResponseDto(pizzaExistant)).thenReturn(pizzaResponseDto);

        // Appeler la méthode à tester
        PizzaResponseDto result = service.modifierPartiellement(id, pizzaRequestDto1());

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(pizzaResponseDto, result);

        // Vérifier que les méthodes simulées ont été appelées
        verify(dao).findById(id);
        verify(mapperMock).toPizza(pizzaRequestDto1());
        verify(dao).save(pizzaExistant);
        verify(mapperMock).toPizzaResponseDto(pizzaExistant);
    }

    @DisplayName("Test modifier partiellement une pizza / pizza non trouvée")
    @Test
    void testModifierPartiellementPizzaNonTrouve() {
        // Préparer les données de test
        int id = 10;
        pizzaRequestDto1();
        // Simuler les appels de méthodes
        when(dao.findById(id)).thenReturn(Optional.empty());

        // Vérifier que l'exception est levée
        assertThrows(EntityNotFoundException.class, () -> service.modifierPartiellement(id, pizzaRequestDto2()));

        // Vérifier que les méthodes simulées ont été appelées
        verify(dao).findById(id);
        verify(mapperMock, never()).toPizza(any());
        verify(dao, never()).save(any());
        verify(mapperMock, never()).toPizzaResponseDto(any());
    }

    @Test
    void trouverToutes(){
        Pizza margherita = margherita();
        Pizza reine = reine();

        List<Pizza> pizzas = List.of(margherita, reine);
        List<PizzaResponseDto> dtos = List.of(pizzaResponseDto1(),pizzaResponseDto2());

        when(dao.findAll()).thenReturn(pizzas);
        when(mapperMock.toPizzaResponseDto(margherita)).thenReturn(pizzaResponseDto1());
        when(mapperMock.toPizzaResponseDto(reine)).thenReturn(pizzaResponseDto2());

        assertEquals(dtos, service.trouverTous());
    }

    @DisplayName("Test rechercher pizza")
    @Test
    void testRechercherPizza() {
        // Préparer les données de test
        Pizza margherita = margherita();
        Pizza reine = reine();
        List<Pizza> pizzas = Arrays.asList(margherita, reine);

        PizzaResponseDto pizzaResponseDto1 = pizzaResponseDto1();
        PizzaResponseDto pizzaResponseDto2 = pizzaResponseDto2();

        // Simuler les appels de méthodes
        when(dao.findAll()).thenReturn(pizzas);
        when(mapperMock.toPizzaResponseDto(margherita)).thenReturn(pizzaResponseDto1);
//        when(mapperMock.toPizzaResponseDto(reine)).thenReturn(pizzaResponseDto2);

        // Appeler la méthode à tester
        List<PizzaResponseDto> result = service.rechercher(null, "Margherita", null);

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Margherita", result.get(0).nom());

        // Vérifier que les méthodes simulées ont été appelées
        verify(dao).findAll();
        verify(mapperMock).toPizzaResponseDto(margherita);
    }

//    ___________________________________________________________
//            METHODES PRIVEES
//    ___________________________________________________________

    private static Pizza margherita(){
        Pizza p = new Pizza();
        p.setNom("Margherita");
        p.setTaille(Taille.PETITE);
        p.setTarif(12.9);
        p.setIngredients(listIingredients());
        return p;
    }

    private static Pizza reine() {
        Pizza pizza = new Pizza("Reine", Taille.MOYENNE, 15.9, listIingredients());
        return pizza;
    }

    private static List<Ingredient> listIingredients() {
        Ingredient tomate = new Ingredient("Tomate", 7);
        Ingredient emmental = new Ingredient("Emmental", 8);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(tomate);
        ingredients.add(emmental);
        return ingredients;
    }

    private static PizzaRequestDto pizzaRequestDto1() {
        return new PizzaRequestDto(1, "Margherita", Taille.PETITE, 12.9, listIingredients());
    }

    private static PizzaRequestDto pizzaRequestDto2() {
        return new PizzaRequestDto(2, "Reine", Taille.MOYENNE, 15.9, listIingredients());
    }

    private static PizzaResponseDto pizzaResponseDto1() {
        return new PizzaResponseDto("Margherita",
                Taille.PETITE,
                12.9,
                listIingredients());
    }
    private static PizzaResponseDto pizzaResponseDto2() {

        return new PizzaResponseDto("Reine",
                Taille.MOYENNE,
                15.9,
                listIingredients());
    }


}
