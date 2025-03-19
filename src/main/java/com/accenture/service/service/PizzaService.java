package com.accenture.service.service;

import com.accenture.exception.PizzaException;
import com.accenture.model.Taille;
import com.accenture.repository.entity.Ingredient;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface PizzaService {
    PizzaResponseDto ajouter(PizzaRequestDto pizzaRequestDto) throws PizzaException;

    PizzaResponseDto modifierPartiellement(Integer id, PizzaRequestDto pizzaRequestDto) throws PizzaException, EntityNotFoundException;

    List<PizzaResponseDto> trouverTous();

    void supprimer(int id) throws EntityNotFoundException;

    List<PizzaResponseDto> rechercher(Integer id, String nom, Ingredient ingredient) throws PizzaException;

    // Méthode pour obtenir le prix d'une pizza en fonction de sa taille
    double getPizzaPrice(Taille taille);

    // Méthode pour définir le prix d'une pizza en fonction de sa taille
    void setPizzaPrice(Taille taille, double prix);

    // Méthode pour augmenter les prix des pizzas
    void increasePizzaPrices(double percentage);

    // Méthode pour diminuer les prix des pizzas
    void decreasePizzaPrices(double percentage);
}
