package com.accenture.model;

import com.accenture.model.Taille;

import java.util.HashMap;
import java.util.Map;

public class PizzaPriceManager {
    private final Map<Taille, Double> pizzaPrices;

    public PizzaPriceManager() {
        pizzaPrices = new HashMap<>();
        double basePrice = 10.99; // Prix de base pour la taille MOYENNE

        pizzaPrices.put(Taille.PETITE, basePrice * 0.8);
        pizzaPrices.put(Taille.MOYENNE, basePrice);
        pizzaPrices.put(Taille.GRANDE, basePrice * 1.2);
    }

    public double getPrice(Taille taille) {
        return pizzaPrices.getOrDefault(taille, 0.0);
    }

    public void setPrice(Taille taille, double prix) {
        pizzaPrices.put(taille, prix);
    }
}