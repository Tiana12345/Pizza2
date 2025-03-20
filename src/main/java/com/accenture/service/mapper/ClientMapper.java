package com.accenture.service.mapper;

import ch.qos.logback.core.net.server.Client;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.ClientRequestDto;
import com.accenture.service.dto.ClientResponseDto;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toClient(ClientRequestDto clientRequestDto);

    ClientResponseDto toClientResponseDto(Client client);
}
