package com.accenture.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientRequestDto(
        @NotBlank(message = "Le nom du client doit être renseigné")
        String nom,
        @NotNull
        @Email(message = "l'adresse e-mail doit être valide")
        String email
) {
}
