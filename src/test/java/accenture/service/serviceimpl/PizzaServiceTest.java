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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        Pizza pizza = margherita();
        pizza.setNom(null);
        PizzaException pe = assertThrows(PizzaException.class, () -> service.ajouter(pizzaRequestDto1()));
        assertEquals("Le nom de la pizza ne peut pas être nul", pe.getMessage());
    }

    @Test
    void ajouterUnePizzaNomBlank() {
        Pizza pizza = margherita();
        pizza.setNom("   \t");
        PizzaException pe = assertThrows(PizzaException.class, () -> service.ajouter(pizzaRequestDto1()));
        assertEquals("Le nom de la pizza ne peut pas être nul", pe.getMessage());
    }

    @Test
    void ajouterPizzaTailleNulle() {
        Pizza pizza = margherita();
        pizza.setTaille(null);
        PizzaException pe = assertThrows(PizzaException.class, () -> service.ajouter(pizzaRequestDto1()));
        assertEquals("Vous devez renseigner la taille de la pizza", pe.getMessage());
    }

    @Test
    void ajouterPizzaTarifNulle() {
        Pizza pizza = margherita();
        pizza.setTarif(null);
        PizzaException pe = assertThrows(PizzaException.class, () -> service.ajouter(pizzaRequestDto1()));
        assertEquals("Le tarif de la pizza doit être renseigné", pe.getMessage());
    }

    @Test
    void ajouterPizzaTarisInf0() {
        Pizza pizza = margherita();
        pizza.setTarif(-1.0);
        PizzaException pe = assertThrows(PizzaException.class, () -> service.ajouter(pizzaRequestDto1()));
        assertEquals("Le tarif de la pizza doit être renseigné", pe.getMessage());
    }
//__________________________________________________________________________

    @Test
    void testModifierPartiellementOk() throws PizzaException, EntityNotFoundException {
        // Préparer les données de test
        int id = 1;
        Pizza pizzaExistant = margherita();
        pizzaRequestDto1() ;
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


//    ___________________________________________________________
//            METHODES PRIVEES
//    ___________________________________________________________

    private static Pizza margherita() {
        Pizza pizza = new Pizza("Margherita", Taille.PETITE, 12.9, listIingredients());
        return pizza;
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
        return new PizzaRequestDto("Margherita", Taille.PETITE, 12.9, listIingredients());
    }

    private static PizzaRequestDto pizzaRequestDto2() {
        PizzaRequestDto requestdto = new PizzaRequestDto("Reine", Taille.MOYENNE, 15.9, listIingredients());
        return requestdto;
    }

    private static PizzaResponseDto pizzaResponseDto2() {
        PizzaResponseDto responseDto = new PizzaResponseDto("Reine", Taille.MOYENNE, 15.9, listIingredients());
        return responseDto;
    }

    private static void pizzaResponseDto1() {
        PizzaResponseDto responseDto = new PizzaResponseDto("Margherita", Taille.PETITE, 12.9, listIingredients());
    }
}
