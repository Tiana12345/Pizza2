package com.accenture.service.mapper;

import com.accenture.repository.entity.Client;
import com.accenture.service.dto.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toClient(ClientDto clientDto);

    ClientDto toClientDto(Client client);
}
