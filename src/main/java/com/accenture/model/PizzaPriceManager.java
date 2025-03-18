package com.accenture.model;

import com.accenture.model.Taille;
import com.accenture.repository.entity.Pizza;

import java.util.HashMap;
import java.util.Map;

public class PizzaPriceManager {
    private final Map<Taille, Double> pizzaPrices;
    private final Pizza pizza;

    public PizzaPriceManager(Pizza pizza) {
        this.pizza = pizza;
        pizzaPrices = new HashMap<>();
        double basePrice = pizza.getTarif(); // Prix de base pour la taille MOYENNE

        pizzaPrices.put(Taille.PETITE, basePrice * 0.8);
        pizzaPrices.put(Taille.MOYENNE, basePrice);
        pizzaPrices.put(Taille.GRANDE, basePrice * 1.2);
    }

    public double getPrice(Taille taille) {
        if (!pizzaPrices.containsKey(taille)) {
            throw new IllegalArgumentException("Taille inconnue : " + taille);
        }
        return pizzaPrices.get(taille);
    }

    public void setPrice(Taille taille, double prix) {
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        pizzaPrices.put(taille, prix);
    }
}