package com.accenture.contoller;

import com.accenture.service.dto.IngredientRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IngredientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPostIngr() throws Exception {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", 13);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.id").value(Matchers.not(0)))
                .andExpect(jsonPath("$.nom").value("Tomate"))
                .andExpect(jsonPath("$.quantite").value(13));
    }

    @Test
    void testPostIngrNomNull() throws Exception {

        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto(null, 13);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("Erreur validation"))
                .andExpect(jsonPath("$.message").value("Le nom de l'ingrédient doit être renseigné"));
    }

    @Test
    void testPostIngrQuantiteNull() throws Exception {

        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/ingredients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("Erreur validation"))
                .andExpect(jsonPath("$.message").value("La quantité de l'ingrédient doit être renseignée"));
    }

    @Test
    void testTrouverTous() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testPatchIngrOK() throws Exception {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate modifiée", 13);
        mockMvc.perform(MockMvcRequestBuilders.patch("/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Tomate modifiée"))
                .andExpect(jsonPath("$.quantite").value(13))
                .andExpect(jsonPath("$.id").value(1));
    }
    @Test
    void testPatchIngrError404() throws Exception {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Ingrédient Inexistant", 30);

        mockMvc.perform(MockMvcRequestBuilders.patch("/ingredients/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("L'id n'existe pas"))
                .andExpect(jsonPath("$.message").value("L'id n'existe pas"));
    }
    @Test
    void testPatchIngrBadRequest() throws Exception {
        IngredientRequestDto ingredientRequestDto = new IngredientRequestDto("Tomate", -1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/ingredients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ingredientRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("Erreur validation"))
                .andExpect(jsonPath("$.message").value( "La quantité doit être supérieure à zéro"));
    }
}
