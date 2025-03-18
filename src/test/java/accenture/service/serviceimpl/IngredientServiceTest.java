package accenture.service.serviceimpl;

import com.accenture.exception.IngredientException;
import com.accenture.repository.dao.IngredientDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.mapper.IngredientMapper;
import org.junit.jupiter.api.Assertions;
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

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @InjectMocks
    private com.accenture.service.IngredientServiceImpl service;
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
        IngredientResponseDto responseDto = creerIngredientResponseDto();

        Mockito.when(mapper.toIngredient(ingredientRequestDto)).thenReturn(ingrAvantEnreg);
        Mockito.when(dao.save(ingrAvantEnreg)).thenReturn(ingrApresEnreg);
        Mockito.when(mapper.toIngredientResponseDto(ingrApresEnreg)).thenReturn(responseDto);
        assertSame(responseDto, service.ajouter(ingredientRequestDto));
        Mockito.verify(dao, Mockito.times(1)).save((ingrAvantEnreg));
    }

    @Test
    void testAjouterIngredientNull () {
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(null));
        Assertions.assertEquals("L'ingrédient doit exister", ex.getMessage());
    }

    @Test
    void testAjouterIngredientNomNull () {
        IngredientRequestDto ingredientRequestDto= new IngredientRequestDto(null,3);
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(ingredientRequestDto));
        Assertions.assertEquals("Le nom de l'ingrédient ne peut pas être nul", ex.getMessage());
    }

    @Test
    void testAjouterIngredienQuantiteNull () {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", null);
        IngredientException ex = assertThrows(IngredientException.class, () -> service.ajouter(ingredientRequestDto));
        Assertions.assertEquals("La quantité de l'ingrédient ne peut pas être nul", ex.getMessage());
    }

       /* @Test
        void testListerIngredient () {
            IngredientRequestDto ingredient1 = creerIngredient1();
            IngredientRequestDto ingredient2 = creerIngredient2();
            List<Ingredient> liste = List.of(creerIngredient1(), creerIngredient2());
            Mockito.when(dao.findAll()).thenReturn(liste);
            assertEquals(liste, service.trouverToutes());
        }

        @Test
        void testModifierQuantite ()

       /* @Test
        void testModifPartielNouveauNom() {
            Client client = creerClient();
            Optional<Client> optionalClient = Optional.of(client);
            Mockito.when(clientDaoMock.findById(1)).thenReturn(optionalClient);
            ClientRequestDto clientRequestDto = creerClientRequestDto();
            Mockito.when(clientMapperMock.toClient(clientRequestDto)).thenReturn(client);
            Client clientNouveauNom = creerClientNouveauNom();
            ClientResponseDto clientResponseDto = creerClientResponseDto();
            Mockito.when(clientDaoMock.save(client)).thenReturn(clientNouveauNom);
            Mockito.when(clientMapperMock.toClientResponseDto(clientNouveauNom)).thenReturn(clientResponseDto);
            assertEquals(clientResponseDto,  service.modifierPartiellement(1, clientRequestDto) );

        }*/





    private static Ingredient creerIngredient1() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1);
        ingredient.setNom("Tomate");
        ingredient.setQuantite(3);
        return ingredient;
    }

    private static Ingredient creerIngredient2() {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(2);
        ingredient.setNom("Olive");
        ingredient.setQuantite(3);
        return ingredient;
    }

    private static IngredientResponseDto creerIngredientResponseDto(){
        return new IngredientResponseDto(1, "Tomate", 3);
    }
}

