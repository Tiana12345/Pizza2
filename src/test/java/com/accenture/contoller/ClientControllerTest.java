package com.accenture.contoller;

import com.accenture.service.dto.ClientDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testPostClient() throws Exception {
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Stiv"))
                .andExpect(jsonPath("$.email").value("stiv@tut.by"));
    }

    @Test
    void testPostNomNull() throws Exception {

        ClientDto clientDto = new ClientDto(null, "stiv@tut.by");

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("Erreur validation"))
                .andExpect(jsonPath("$.message").value("Le nom du client doit être renseigné"));
    }

    @Test
    void testPostEmailNull() throws Exception {

        ClientDto clientDto = new ClientDto("Stiv", null);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("Erreur validation"))
                .andExpect(jsonPath("$.message").value("L'email du client doit être renseigné"));
    }

    @Test
    void testTrouverTous() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testPutOK() throws Exception {
        ClientDto clientDto = new ClientDto("Stiv modifié", "stiv@tut.by");
        mockMvc.perform(MockMvcRequestBuilders.put("/clients/stiv@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Stiv modifié"));

    }

    @Test
    void testPutClientError404() throws Exception {
        ClientDto clientDto = new ClientDto("Client Inexistant", "tanya@tut.by");

        mockMvc.perform(MockMvcRequestBuilders.put("/clients/tanya@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("L'id n'existe pas"))
                .andExpect(jsonPath("$.message").value("L'id n'existe pas"));
    }

    @Test
    void testPutClientBadRequest() throws Exception {
        ClientDto clientDto = new ClientDto("", "stiv@tut.by");

        mockMvc.perform(MockMvcRequestBuilders.put("/clients/stiv@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("Erreur validation"))
                .andExpect(jsonPath("$.message").value("Le nom du client doit être renseigné"));
    }

    @Test
    void testTrouverOk() throws Exception {
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/stiv@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Stiv"))
                .andExpect(jsonPath("$.email").value("stiv@tut.by"));

    }

    @Test
    void testTrouverPasOk() throws Exception {
        ClientDto clientDto = new ClientDto("Client inexistant", "tanya@tut.by");
        mockMvc.perform(MockMvcRequestBuilders.get("/clients/tanya@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("L'id n'existe pas"))
                .andExpect(jsonPath("$.message").value("Ce mail n'existe pas dans notre base de données"));
    }

    @Test
    void testDeleteOK() throws Exception {
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/stiv@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePasOK() throws Exception {
        ClientDto clientDto = new ClientDto("Client inexistant", "tanya@tut.by");
        mockMvc.perform(MockMvcRequestBuilders.delete("/clients/tanya@tut.by")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("L'id n'existe pas"))
                .andExpect(jsonPath("$.message").value("L'id n'existe pas"));

    }
}