package accenture.controller;

import com.accenture.controller.PizzaController;
import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;


@WebMvcTest(controllers = {PizzaController.class})
public class PizzaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PizzaService pizzaService;
    @MockitoBean
    private PizzaController pizzaController;

    @Test
    void testTrouverTous() throws Exception {

    }

    //_________________________________________________
    private static Pizza margherita() {
        Pizza p = new Pizza();
        p.setNom("Margherita");
        p.setTaille(Taille.PETITE);
        p.setTarif(12.9);
        p.setIngredients(listIingredients());
        return p;
    }

    private static Pizza reine() {
        Pizza p2 = new Pizza();
        p2.setNom("Reine");
        p2.setTaille(Taille.MOYENNE);
        p2.setTarif(15.9);
        p2.setIngredients(listIingredients());
        return p2;
    }

    private static List<Ingredient> listIingredients() {
        Ingredient tomate = new Ingredient("Tomate", 7);
        Ingredient emmental = new Ingredient("Emmental", 8);
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(tomate);
        ingredients.add(emmental);
        return ingredients;
    }

}
