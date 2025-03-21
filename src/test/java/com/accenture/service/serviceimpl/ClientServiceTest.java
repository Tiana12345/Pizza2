package com.accenture.service.serviceimpl;

import com.accenture.exception.ClientException;
import com.accenture.repository.dao.ClientDao;
import com.accenture.repository.entity.Client;
import com.accenture.service.dto.ClientDto;
import com.accenture.service.mapper.ClientMapper;
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
public class ClientServiceTest {
    @InjectMocks
    private ClientServiceImpl service;
    @Mock
    private ClientDao dao;
    @Mock
    private ClientMapper mapper;

    @Test
    void testAjouterOK() {
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        Client clientAvantEnreg = new Client("Stiv", "stiv@tut.by");
        Client clientApresEnreg = new Client("Stiv", "stiv@tut.by");
        ClientDto responseDto = new ClientDto("Stiv", "stiv@tut.by");

        Mockito.when(mapper.toClient(clientDto)).thenReturn(clientAvantEnreg);
        Mockito.when(dao.save(clientAvantEnreg)).thenReturn(clientApresEnreg);
        Mockito.when(mapper.toClientDto(clientApresEnreg)).thenReturn(responseDto);
        assertSame(responseDto, service.ajouter(clientDto));
        Mockito.verify(dao, Mockito.times(1)).save((clientAvantEnreg));
    }

    @Test
    void testAjouterIngredientNull() {
        ClientException ex = assertThrows(ClientException.class, () -> service.ajouter(null));
        Assertions.assertEquals("Le client doit exister", ex.getMessage());
    }

    @Test
    void testAjouterIngredientNomNull() {
        ClientDto clientDto = new ClientDto(null, "stiv@tut.by");
        ClientException ex = assertThrows(ClientException.class, () -> service.ajouter(clientDto));
        Assertions.assertEquals("Le nom du client doit être renseigné", ex.getMessage());
    }

    @Test
    void testAjouterIngredientNomBlank() {
        ClientDto clientDto = new ClientDto("", "stiv@tut.by");
        ClientException ex = assertThrows(ClientException.class, () -> service.ajouter(clientDto));
        Assertions.assertEquals("Le nom du client doit être renseigné", ex.getMessage());
    }

    @Test
    void testAjouterEmailNull() {
        ClientDto clientDto = new ClientDto("Stiv", null);
        ClientException ex = assertThrows(ClientException.class, () -> service.ajouter(clientDto));
        Assertions.assertEquals("L'email doit être renseigné", ex.getMessage());
    }

    @Test
    void testAjouterEmailBlank() {
        ClientDto clientDto = new ClientDto("Stiv", "\t");
        ClientException ex = assertThrows(ClientException.class, () -> service.ajouter(clientDto));
        Assertions.assertEquals("L'email doit être renseigné", ex.getMessage());
    }

    @Test
    void testAjouterEmailFauxFormat() {
        ClientDto clientDto = new ClientDto("Stiv", "fgjjk");
        ClientException ex = assertThrows(ClientException.class, () -> service.ajouter(clientDto));
        Assertions.assertEquals("L'email est obligatoire et doit être écrit en format email", ex.getMessage());
    }

    @Test
    void testTrouverTousOk() {
        Client client = new Client("Stiv", "stiv@tut.by");
        Client client1 = new Client("Masha", "masha@tut.by");
        List<Client> list = List.of(client, client1);
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        ClientDto clientDto1 = new ClientDto("Masha", "masha@tut.by");
        List<ClientDto> dtos = List.of(clientDto, clientDto1);
        Mockito.when(dao.findAll()).thenReturn(list);
        Mockito.when(mapper.toClientDto(client)).thenReturn(clientDto);
        Mockito.when(mapper.toClientDto(client1)).thenReturn(clientDto1);
        assertEquals(dtos, service.trouverTous());
    }

    @Test
    void testTrouverByEmailExiste() {
        Client client = new Client("Stiv", "stiv@tut.by");
        Optional<Client> optClient = Optional.of(client);
        Mockito.when(dao.findById("stiv@tut.by")).thenReturn(optClient);
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        Mockito.when(mapper.toClientDto(client)).thenReturn(clientDto);
        assertSame(clientDto, service.trouverByEmail("stiv@tut.by"));
    }

    @Test
    void testTrouverByEmailExistePas() {
        Mockito.when(dao.findById("stiv@tut.by")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.trouverByEmail("stiv@tut.by"));
        assertEquals("Ce mail n'existe pas dans notre base de données", ex.getMessage());
    }

    @Test
    void testSupprimerClientExiste() {
        Mockito.when(dao.existsById("stiv@tut.by")).thenReturn(true);
        service.supprimer("stiv@tut.by");
        Mockito.verify(dao, Mockito.times(1)).deleteById("stiv@tut.by");
    }

    @Test
    void testSupprimerClientExistePas() {
        String email = "stiv@tut.by";
        Mockito.when(dao.existsById("stiv@tut.by")).thenReturn(false);
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.supprimer(email));
        assertEquals("L'id n'existe pas", ex.getMessage());
    }

    @Test
    void testModifierClientExistePas() {
        Mockito.when(dao.findById("stiv@tut.by")).thenReturn(Optional.empty());
        ClientDto clientDto = new ClientDto("Stiv", "stiv@tut.by");
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> service.modifier(clientDto));
        assertEquals("L'id n'existe pas", ex.getMessage());
    }

    @Test
    void modifierClientNull() {
        assertThrows(ClientException.class, () -> service.modifier(null));
    }
    @Test
    void modifierNom(){
        Client client = new Client("Stiv", "stiv@tut.by");
        Optional<Client> optionalClient = Optional.of(client);
        Mockito.when(dao.findById("stiv@tut.by")).thenReturn(optionalClient);
        ClientDto clientDto = new ClientDto("Stiv-John", "stiv@tut.by");
        Mockito.when(mapper.toClient(clientDto)).thenReturn(client);
        Client clientNouveauNom =  new Client("Stiv-John", "stiv@tut.by");
        ClientDto clientDtoNouveauNom =  new ClientDto("Stiv-John", "stiv@tut.by");
        Mockito.when(dao.save(client)).thenReturn(clientNouveauNom);
        Mockito.when(mapper.toClientDto(clientNouveauNom)).thenReturn(clientDtoNouveauNom);
        assertEquals(clientDto,  service.modifier(clientDto) );
    }
}
