package com.accenture.controller;

import com.accenture.service.dto.ClientDto;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@Tag(name = "Clients", description = "Gestion des clients")
@RequestMapping("/clients")
public class ClientController {
    private ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PutMapping("/{email}")
    @Operation(summary = "Modifier le nom du client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Le client modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide, paramètre manquant ou mal formaté "),
            @ApiResponse(responseCode = "404", description = "Le client non trouvé pour l'id spécifié"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    ResponseEntity<ClientDto> modifier(@PathVariable("email") String email, @Valid @RequestBody ClientDto clientDto) {
       ClientDto response = service.modifier( clientDto);
        log.info("Le client modifié avec succès");
        return ResponseEntity.ok(response);
    }
    @PostMapping
    @Operation(summary = "Ajouter un nouveau client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Le client est créé avec succès"),
            @ApiResponse(responseCode = "400", description = "La requête est invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    ResponseEntity<ClientDto> ajouter(@Valid @RequestBody ClientDto clientDto) {
        ClientDto clientEnreg = service.ajouter(clientDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{email}")
                .buildAndExpand(clientEnreg.email())
                .toUri();
        log.info("Client ajouté avec succès, ID : {}", clientEnreg.email());
        return ResponseEntity.created(location).body(clientEnreg);
    }
    @GetMapping
    @Operation(summary = "Afficher tous les clients")
    @ApiResponse(responseCode = "200", description = "Liste des clients récupérée avec succès")
    List<ClientDto> listerClients(){
        log.info("Clients affichés avec succès");
        return service.trouverTous();
    }
    @DeleteMapping("/{email}")
    @Operation(summary = "Supprimer un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Le email n'est pas trouvé"),
            @ApiResponse(responseCode = "400", description = "Requête invalide, l'email est mal formaté ou manquant"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur lors de la suppression")
    })
    ResponseEntity<Void> supprimer(@PathVariable("email") String email) {
        service.supprimer(email);
        log.info("Le client bien supprimé");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @GetMapping("/{email}")
    @Operation(summary = "Afficher un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client trouvé avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide,email ou mot de passe mal formatés "),
            @ApiResponse(responseCode = "404", description = "Client non trouvé pour les identifiants fournis"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    ResponseEntity<ClientDto> afficherClient(@PathVariable("email") String email) {
        ClientDto trouve = service.trouverByEmail(email);
        log.info("Le client est trouvé avec succès");
        return ResponseEntity.ok(trouve);
    }
}
