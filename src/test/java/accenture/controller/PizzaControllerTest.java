package accenture.controller;

import com.accenture.controller.PizzaController;
import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.service.PizzaService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

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

}
