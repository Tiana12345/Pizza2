package com.accenture.contoller;

import com.accenture.model.Taille;
import com.accenture.service.dto.PizzaResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PizzaControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void ajouterPizza() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/pizzas/ajouter")
                        .param("nom", "Margherita")
                        .param("petiteTarif", "10.0")
                        .param("moyenneTarif", "15.0")
                        .param("grandeTarif", "20.0")
                        .param("ingrs", "1,2,3"))
                .andExpect(status().isOk());
    }

    @Test
    void ajouterPizzaNOK() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/pizzas/ajouter")
                        .param("nom", "")
                        .param("petiteTarif", "10.0")
                        .param("moyenneTarif", "15.0")
                        .param("grandeTarif", "20.0")
                        .param("ingrs", "1,2,3"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void trouverToutesPizzas() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pizzas"))
                .andExpect(status().isOk());
    }

    @Test
    void modifierPartiellementPizza() throws Exception {
        // Crée le corps JSON pour la requête PATCH
        String pizzaJson = "{\"nom\":\"Margherita\",\"tarif\":{\"PETITE\":12.0,\"MOYENNE\":18.0,\"GRANDE\":25.0},\"ingrs\":[1,2,3]}";

        // Configure le comportement du mock
        Map<Taille, Double> tarif = new HashMap<>();
        tarif.put(Taille.PETITE, 12.0);
        tarif.put(Taille.MOYENNE, 18.0);
        tarif.put(Taille.GRANDE, 25.0);
        PizzaResponseDto pizzaResponseDto = new PizzaResponseDto(1,"Margherita", tarif, Arrays.asList("Tomate", "Fromage", "Basilic"));

        // Effectue la requête PATCH avec le corps JSON
        mockMvc.perform(MockMvcRequestBuilders.patch("/pizzas/1")
                        .contentType("application/json")
                        .content(pizzaJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Margherita"))
                .andExpect(jsonPath("$.tarif.PETITE").value(12.0))
                .andExpect(jsonPath("$.tarif.MOYENNE").value(18.0))
                .andExpect(jsonPath("$.tarif.GRANDE").value(25.0))
                .andExpect(jsonPath("$.ingredients[0]").value("Tomate"))
                .andExpect(jsonPath("$.ingredients[1]").value("OLive"))
                .andExpect(jsonPath("$.ingredients[2]").value("Fromage"));
    }

    @Test
    void modifierPartiellementPizzaNOK() throws Exception {
        // Crée le corps JSON pour la requête PATCH avec un nom nul
        String pizzaJson = "{\"nom\":\"\",\"tarif\":{\"PETITE\":12.0,\"MOYENNE\":18.0,\"GRANDE\":25.0},\"ingrs\":[1,2,3]}";

        // Effectue la requête PATCH avec le corps JSON
        mockMvc.perform(MockMvcRequestBuilders.patch("/pizzas/1")
                        .contentType("application/json")
                        .content(pizzaJson))
                .andExpect(status().isBadRequest());
    }
@Test
void supprimerPizza() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/pizzas/1"))
            .andExpect(status().isNoContent());
}
@Test
void supprimerPizzaNOK() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/pizzas/99"))
            .andExpect(status().isNotFound());
}

@Test
void testTrouverParNom() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/pizzas/search?nom=Margherita"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(jsonPath("$.length()")
                    .value(1));
}
    @Test
    void testTrouverParId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pizzas/search?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testTrouverParIngredient() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/pizzas/search?ingredientNom=Tomate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

}