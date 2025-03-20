package com.accenture.service.service;

import com.accenture.exception.ClientException;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface ClientService {
    ClientResponseDto ajouterClient(ClientRequestDto clientRequestDto) throws ClientException;

    List<ClientResponseDto> trouverTousClients();

    ClientResponseDto trouverByEmail(String email);

    void supprimer(int id) throws EntityNotFoundException;

    ClientResponseDto modifierPartiellement(String email, ClientRequestDto clientRequestDto) throws ClientException;
}
