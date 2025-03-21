package com.accenture.service.service;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.ClientDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {
    ClientDto ajouter(ClientDto clientDto) throws ClientException;

    List<ClientDto> trouverTous ();

    ClientDto trouverByEmail(String email);

    void supprimer(String email) throws EntityNotFoundException;

    ClientDto modifier(ClientDto clientDto) throws ClientException;
}
