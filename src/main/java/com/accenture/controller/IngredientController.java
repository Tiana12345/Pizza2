package com.accenture.controller;

import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.IngredientRequestDto;
import com.accenture.service.dto.IngredientResponseDto;
import com.accenture.service.service.IngredientService;
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
@Tag(name = "Ingredients", description = "Gestion des ingrédients")
@RequestMapping("/ingredients")
public class IngredientController {
    private IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Ajouter un nouveau ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingrédient est créé avec succès"),
            @ApiResponse(responseCode = "400", description = "La requête est invalide"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    ResponseEntity<IngredientResponseDto> ajouter(@Valid @RequestBody IngredientRequestDto ingredientRequestDto) {
        IngredientResponseDto ingrEnreg = service.ajouter(ingredientRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ingrEnreg.id())
                .toUri();
        log.info("Ingrédient ajouté avec succès, ID : {}", ingrEnreg.id());
        return ResponseEntity.created(location).body(ingrEnreg);
    }
    @GetMapping
    @Operation(summary = "Afficher tous les ingrédients")
    @ApiResponse(responseCode = "200", description = "Liste des ingrédients récupérée avec succès")
    List<IngredientResponseDto> listerIngr(){
        log.info("Ingrédients affichés avec succès");
        return service.trouverTous();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier un ingrédient partiellement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "L'ingrédient modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide, paramètre manquant ou mal formaté "),
            @ApiResponse(responseCode = "404", description = "L'ingrédient non trouvé pour l'id spécifié"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    ResponseEntity<IngredientResponseDto> modifierPartiellement(@PathVariable("id") int id, @Valid @RequestBody IngredientRequestDto ingredientRequestDto){
        IngredientResponseDto response = service.modifierPartiellement(id, ingredientRequestDto);
        log.info("L'ingrédient modifié avec succès");
        return ResponseEntity.ok(response);
    }
}
