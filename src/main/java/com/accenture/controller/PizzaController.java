package com.accenture.controller;

import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.service.serviceimpl.PizzaServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pizzas")
@Tag(name = "Pizzas", description = "Gestion des pizzas")
public class PizzaController {
    private final PizzaServiceImpl pizzaService;

    public PizzaController(PizzaServiceImpl pizzaService) {
        this.pizzaService = pizzaService;
    }

    @PostMapping("/ajouter")
    @Operation(summary = "Ajouter une nouvelle pizza", description = "Ajoute une nouvelle pizza à la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pizza créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    public ResponseEntity<Void> ajouter(
            @Parameter(description = "Nom de la pizza") @RequestParam(required = true) String nom,
            @Parameter(description = "Tarif de la pizza pour PETITE taille") @RequestParam(required = true) Double petiteTarif,
            @Parameter(description = "Tarif de la pizza pour MOYENNE taille") @RequestParam(required = true) Double moyenneTarif,
            @Parameter(description = "Tarif de la pizza pour GRANDE taille") @RequestParam(required = true) Double grandeTarif,
            @Parameter(description = "Liste des ingrédients de la pizza") @RequestParam(required = true) List<Integer> ingrs) {

        // Créez la carte tarif
        Map<Taille, Double> tarif = new HashMap<>();
        tarif.put(Taille.PETITE, petiteTarif);
        tarif.put(Taille.MOYENNE, moyenneTarif);
        tarif.put(Taille.GRANDE, grandeTarif);

        // Créez votre DTO avec les valeurs des paramètres
        PizzaRequestDto pizzaRequestDto = new PizzaRequestDto(nom, tarif, ingrs);

        // Traitez le DTO comme nécessaire
        PizzaResponseDto pizzaEnreg = pizzaService.ajouter(pizzaRequestDto);
        return ResponseEntity.ok().build();
    }



    @GetMapping
    @Operation(summary = "Obtenir toutes les pizzas", description = "Récupère la liste de toutes les pizzas disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des pizzas récupérée avec succès")
    })
    public List<PizzaResponseDto> toutesLesPizzas() {
        log.info("Récupération de toutes les pizzas");
        List<PizzaResponseDto> pizzas = pizzaService.trouverTous();
        log.info("Nombre de pizzas récupérées : {}", pizzas.size());
        return pizzas;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une pizza", description = "Supprime une pizza de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pizza supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Pizza non trouvée")
    })
    public ResponseEntity<Void> supprimer(@Parameter(description = "ID de la pizza à supprimer") @PathVariable("id") int id) {
        log.info("Suppression de la pizza avec ID : {}", id);
        pizzaService.supprimer(id);
        log.info("Pizza supprimée avec succès : {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Modifier partiellement une pizza", description = "Met à jour partiellement les détails d'une pizza existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza mise à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Pizza non trouvée")
    })
    public ResponseEntity<PizzaResponseDto> modifierPartiellement(
            @Parameter(description = "ID de la pizza à modifier", required = true) @PathVariable("id") int id,
            @Parameter(description = "Détails de la pizza à modifier", required = true) @RequestBody PizzaRequestDto pizzaRequestDto) {
        log.info("Modification partielle de la pizza avec ID : {}", id);
        PizzaResponseDto pizzaModifie = pizzaService.modifierPartiellement(id, pizzaRequestDto);
        log.info("Pizza mise à jour avec succès : {}", pizzaModifie);
        return ResponseEntity.ok(pizzaModifie);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des pizzas", description = "Recherche des pizzas en fonction de différents critères")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    })
    public List<PizzaResponseDto> recherche(
            @Parameter(description = "ID de la pizza") @RequestParam(required = false) Integer id,
            @Parameter(description = "Nom de la pizza") @RequestParam(required = false) String nom,
            @Parameter(description = "Nom de l'ingrédient") @RequestParam(required = false) String ingredientNom) {
        log.info("Recherche de pizzas avec les critères : id={}, nom={}, ingredientNom={}", id, nom, ingredientNom);

        Ingredient ingredient = null;
        if (ingredientNom != null) {
            ingredient = new Ingredient();
            ingredient.setNom(ingredientNom);
        }

        List<PizzaResponseDto> pizzas = pizzaService.rechercher(id, nom, ingredient);
        log.info("Nombre de pizzas trouvées : {}", pizzas.size());
        return pizzas;
    }
}