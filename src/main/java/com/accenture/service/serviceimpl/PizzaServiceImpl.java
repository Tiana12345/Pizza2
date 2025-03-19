package com.accenture.service.serviceimpl;

import com.accenture.exception.PizzaException;
import com.accenture.model.Taille;
import com.accenture.repository.dao.IngredientDao;
import com.accenture.repository.dao.PizzaDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PizzaServiceImpl {

    private final PizzaDao pizzaDao;
    private final IngredientDao ingredientDao;


    public PizzaServiceImpl(PizzaDao pizzaDao, IngredientDao ingredientDao) {
        this.pizzaDao = pizzaDao;
        this.ingredientDao = ingredientDao;
    }

    public PizzaResponseDto ajouter(PizzaRequestDto pizzaRequestDto) throws PizzaException {
        verifPizza(pizzaRequestDto);
        List<Ingredient> ingredients = ingredientDao.findAllById(pizzaRequestDto.ingrs());

        Map<Taille, Double> tarifMap = new HashMap<>();
        tarifMap.put(Taille.PETITE, 9.0);
        tarifMap.put(Taille.MOYENNE, 13.0);
        tarifMap.put(Taille.GRANDE, 17.0);


        Pizza pizza = new Pizza(pizzaRequestDto.nom(), tarifMap, ingredients);
        Pizza pizzaEnreg = pizzaDao.save(pizza);

        List<String> ingrs = pizzaEnreg.getIngredients().stream()
                .map(Ingredient::getNom)
                .toList();

        return new PizzaResponseDto(pizzaEnreg.getId(), pizzaEnreg.getNom(), tarifMap, ingrs);

    }

    public PizzaResponseDto modifierPartiellement(int id, PizzaRequestDto pizzaRequestDto) throws PizzaException, EntityNotFoundException {

        Optional<Pizza> optPizza = pizzaDao.findById(id);
        if (optPizza.isEmpty())
            throw new EntityNotFoundException("Erreur, l'identifiant ne correspond a aucune pizza");
        Pizza pizzaExistant = optPizza.get();

        verifPizza(pizzaRequestDto);
        List<Ingredient> ingredients = ingredientDao.findAllById(pizzaRequestDto.ingrs());

        Pizza nouveau = new Pizza(pizzaRequestDto.nom(), pizzaRequestDto.tarif(), ingredients);
        remplacer(nouveau, pizzaExistant);

        if (nouveau.getNom() == null || nouveau.getNom().isBlank()) {
            throw new PizzaException("Le nom de la pizza ne peut pas être nul ou vide. ");
        }

        Pizza pizzaEnreg = pizzaDao.save(pizzaExistant);

        List<String> ingres = pizzaEnreg.getIngredients().stream()
                .map(Ingredient::getNom)
                .toList();
        PizzaResponseDto dto = new PizzaResponseDto(pizzaEnreg.getId(), pizzaEnreg.getNom(), pizzaEnreg.getTarif(), ingres);
        return dto;
    }

    public List<PizzaResponseDto> trouverTous() {
        return pizzaDao.findAll().stream()
                .map(pizza -> new PizzaResponseDto(
                        pizza.getId(),
                        pizza.getNom(),
                        pizza.getTarif(),
                        pizza.getIngredients().stream()
                                .map(Ingredient::getNom)
                                .toList()
                ))
                .toList();
    }

    public void supprimer(int id) throws EntityNotFoundException {
        if (pizzaDao.existsById(id))
            pizzaDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucune pizza enregistrée avec cet id");
    }

    public List<PizzaResponseDto> rechercher(Integer id, String nom, Ingredient ingredient) throws PizzaException {
        List<Pizza> liste = pizzaDao.findAll();

        liste = recherches(id, nom, ingredient, liste);

        return liste.stream()
                .map(pizza -> new PizzaResponseDto(
                        pizza.getId(),
                        pizza.getNom(),
                        pizza.getTarif(),
                        pizza.getIngredients().stream()
                                .map(Ingredient::getNom)
                                .toList()
                )).toList();
    }


    //    _______________________________________________________________________________________________
//            METHODES PRIVEES
//    ______________________________________________________________________________________________
    private static void verifPizza(PizzaRequestDto pizzaRequestDto) {
        if (pizzaRequestDto == null)
            throw new PizzaException("La pizza doit exister");
        if (pizzaRequestDto.nom() == null || pizzaRequestDto.nom().isBlank())
            throw new PizzaException("Le nom de la pizza ne peut pas être nul");
        if (pizzaRequestDto.ingrs() == null)
            throw new PizzaException("La pizza contient obligatoirement des ingrédients");
        if (pizzaRequestDto.tarif() == null || pizzaRequestDto.tarif().containsValue(0.0))
            throw new PizzaException("Le tarif de la pizza doit être renseigné");
    }

    private static void remplacer(Pizza pizza, Pizza pizzaExistant) {

        if (pizza.getNom() != null && !pizza.getNom().isBlank())
            pizzaExistant.setNom(pizza.getNom());
        if (pizza.getTarif() != null)
            pizzaExistant.setTarif(pizza.getTarif());
        if (pizza.getIngredients() != null)
            pizzaExistant.setIngredients(pizza.getIngredients());
    }

    private static List<Pizza> recherches(Integer id, String nom, Ingredient ingredient, List<Pizza> liste) {
        if (id != null && id > 0) {
            liste = liste.stream()
                    .filter(pizza -> pizza.getId() == id)
                    .toList();
        }
        if (nom != null) {
            liste = liste.stream()
                    .filter(pizza -> pizza.getNom().contains(nom))
                    .toList();
        }
        if (ingredient != null) {
            liste = liste.stream()
                    .filter(pizza -> !pizza.getIngredients()
                            .stream()
                            .filter(ingr -> ingr.getNom().equals(ingredient.getNom()))
                            .toList().isEmpty())
                    .toList();
        }
        return liste;
    }

}