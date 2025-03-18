package com.accenture.service.serviceimpl;

import com.accenture.exception.PizzaException;
import com.accenture.repository.dao.PizzaDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.service.mapper.PizzaMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaServiceImpl {

    private final PizzaDao pizzaDao;
    private final PizzaMapper pizzaMapper;


    public PizzaServiceImpl(PizzaDao pizzaDao, PizzaMapper pizzaMapper) {
        this.pizzaDao = pizzaDao;
        this.pizzaMapper = pizzaMapper;
    }

    public PizzaResponseDto ajouter(PizzaRequestDto pizzaRequestDto) throws PizzaException {
        verifPizza(pizzaRequestDto);

        Pizza pizza = pizzaMapper.toPizza(pizzaRequestDto);
        Pizza pizzaEnreg = pizzaDao.save(pizza);

        return pizzaMapper.toPizzaResponseDto(pizzaEnreg);

    }

    public PizzaResponseDto modifierPartiellement(int id, PizzaRequestDto pizzaRequestDto) throws PizzaException, EntityNotFoundException {

        Optional<Pizza> optPizza = pizzaDao.findById(id);
        if (optPizza.isEmpty())
            throw new EntityNotFoundException("Erreur, l'identifiant ne correspond a aucune pizza");
        Pizza pizzaExistant = optPizza.get();

        verifPizza(pizzaRequestDto);

        Pizza nouveau = pizzaMapper.toPizza(pizzaRequestDto);
        remplacer(nouveau, pizzaExistant);

        if (nouveau.getNom() == null || nouveau.getNom().isBlank()) {
            throw new PizzaException("Le nom de la pizza ne peut pas être nul ou vide. ");
        }

        Pizza pizzaEnreg = pizzaDao.save(pizzaExistant);
        return pizzaMapper.toPizzaResponseDto(pizzaEnreg);
    }

    public List<PizzaResponseDto> trouverTous(){
        return pizzaDao.findAll().stream()
                .map(pizzaMapper::toPizzaResponseDto)
                .toList();
    }

    public void supprimer(int id) throws EntityNotFoundException{
        if (pizzaDao.existsById(id))
            pizzaDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucune pizza enregistrée avec cet id");
    }

    public List<PizzaResponseDto> rechercher (Integer id, String nom, Ingredient ingredient) throws PizzaException{
        List<Pizza> liste = pizzaDao.findAll();

        liste = recherches(id, nom, ingredient, liste);

        return liste.stream()
                .map(pizzaMapper::toPizzaResponseDto)
                .toList();
    }


    //    _______________________________________________________________________________________________
//            METHODES PRIVEES
//    ______________________________________________________________________________________________
    private static void verifPizza(PizzaRequestDto pizzaRequestDto) {
        if (pizzaRequestDto == null)
            throw new PizzaException("La pizza doit exister");
        if (pizzaRequestDto.nom() == null || pizzaRequestDto.nom().isBlank())
            throw new PizzaException("Le nom de la pizza ne peut pas être nul");
        if (pizzaRequestDto.taille() == null)
            throw new PizzaException("Vous devez renseigner la taille de la pizza");
        if (pizzaRequestDto.ingredients() == null)
            throw new PizzaException("La taille de la pizza est obligatoire");
        if (pizzaRequestDto.tarif() == null || pizzaRequestDto.tarif() < 0)
            throw new PizzaException("Le tarif de la pizza doit être renseigné");
    }

    private static void remplacer(Pizza pizza, Pizza pizzaExistant) {

        if (pizza.getNom() != null && !pizza.getNom().isBlank())
            pizzaExistant.setNom(pizza.getNom());
        if (pizza.getTaille() != null)
            pizzaExistant.setTaille(pizza.getTaille());
        if (pizza.getTarif() != null)
            pizzaExistant.setTarif(pizza.getTarif());
        if (pizza.getIngredients() != null)
            pizzaExistant.setIngredients(pizza.getIngredients());
    }

    private static List<Pizza> recherches(Integer id, String nom, Ingredient ingredient, List<Pizza> liste) {
        if (id != null && id > 0){
            liste = liste.stream()
                    .filter(pizza -> pizza.getId() == id)
                    .toList();
        }
        if (nom != null){
            liste = liste.stream()
                    .filter(pizza -> pizza.getNom().contains(nom))
                    .toList();
        }
        if (ingredient != null){
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
