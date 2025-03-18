package com.accenture.repository.entity;

import com.accenture.model.Taille;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String nom;
    @ManyToMany
    List<Ingredient> ingredients;
    @Enumerated(EnumType.STRING)
    Taille taille;
    Double tarif;

    public Pizza(String nom, Taille taille, Double tarif, List<Ingredient> ingredients) {
        this.nom = nom;
        this.taille = taille;
        this.tarif = tarif;
        this.ingredients = ingredients;
    }
}
