package com.accenture.repository.entity;

import com.accenture.model.Taille;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

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

    @ElementCollection
    @CollectionTable(name = "pizza_taille_map", joinColumns = @JoinColumn(name = "entity_id"))
    @MapKeyEnumerated(EnumType.STRING) // Stocke la clé de l'EnumMap sous forme de chaîne
    @Column(name = "tarif") // Valeur stockée
    Map<Taille, Double> tarif;

    public Pizza(String nom, Map<Taille, Double> tarif, List<Ingredient> ingredients) {
        this.nom = nom;
        this.tarif = tarif;
        this.ingredients = ingredients;
    }
}
