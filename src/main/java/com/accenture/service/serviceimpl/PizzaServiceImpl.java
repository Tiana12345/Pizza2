package com.accenture.service.serviceimpl;

import com.accenture.exception.PizzaException;
import com.accenture.model.PizzaPriceManager;
import com.accenture.repository.dao.PizzaDao;
import com.accenture.repository.entity.Ingredient;
import com.accenture.repository.entity.Pizza;
import com.accenture.service.dto.PizzaRequestDto;
import com.accenture.service.dto.PizzaResponseDto;
import com.accenture.service.mapper.PizzaMapper;
import com.accenture.model.Taille;
import com.accenture.service.service.PizzaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaServiceImpl implements PizzaService {

    private final PizzaDao pizzaDao;
    private final PizzaMapper pizzaMapper;
    private final PizzaPriceManager priceManager;

    public PizzaServiceImpl(PizzaDao pizzaDao, PizzaMapper pizzaMapper) {
        this.pizzaDao = pizzaDao;
        this.pizzaMapper = pizzaMapper;
        this.priceManager = new PizzaPriceManager(); // Initialiser le gestionnaire de prix des pizzas
    }

    @Override
    public PizzaResponseDto ajouter(PizzaRequestDto pizzaRequestDto) throws PizzaException {
        verifPizza(pizzaRequestDto);

        Pizza pizza = pizzaMapper.toPizza(pizzaRequestDto);
        pizza.setTarif(priceManager.getPrice(pizza.getTaille())); // Utiliser le gestionnaire de prix
        Pizza pizzaEnreg = pizzaDao.save(pizza);

        return pizzaMapper.toPizzaResponseDto(pizzaEnreg);
    }

    @Override
    public PizzaResponseDto modifierPartiellement(Integer id, PizzaRequestDto pizzaRequestDto) throws PizzaException, EntityNotFoundException {
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

        pizzaExistant.setTarif(priceManager.getPrice(pizzaExistant.getTaille())); // Utiliser le gestionnaire de prix
        Pizza pizzaEnreg = pizzaDao.save(pizzaExistant);
        return pizzaMapper.toPizzaResponseDto(pizzaEnreg);
    }

    @Override
    public List<PizzaResponseDto> trouverTous() {
        return pizzaDao.findAll().stream()
                .map(pizzaMapper::toPizzaResponseDto)
                .toList();
    }

    @Override
    public void supprimer(int id) throws EntityNotFoundException {
        if (pizzaDao.existsById(id))
            pizzaDao.deleteById(id);
        else
            throw new EntityNotFoundException("Aucune pizza enregistrée avec cet id");
    }

    @Override
    public List<PizzaResponseDto> rechercher(Integer id, String nom, Ingredient ingredient) throws PizzaException {
        List<Pizza> liste = pizzaDao.findAll();

        liste = recherches(id, nom, ingredient, liste);

        return liste.stream()
                .map(pizzaMapper::toPizzaResponseDto)
                .toList();
    }

    // Méthode pour obtenir le prix d'une pizza en fonction de sa taille
    @Override
    public double getPizzaPrice(Taille taille) {
        return priceManager.getPrice(taille);
    }

    // Méthode pour définir le prix d'une pizza en fonction de sa taille
    @Override
    public void setPizzaPrice(Taille taille, double prix) {
        priceManager.setPrice(taille, prix);
    }

    // Méthode pour augmenter les prix des pizzas
    @Override
    public void increasePizzaPrices(double percentage) {
        for (Taille taille : Taille.values()) {
            double currentPrice = priceManager.getPrice(taille);
            priceManager.setPrice(taille, currentPrice * (1 + percentage / 100));
        }
    }

    // Méthode pour diminuer les prix des pizzas
    @Override
    public void decreasePizzaPrices(double percentage) {
        for (Taille taille : Taille.values()) {
            double currentPrice = priceManager.getPrice(taille);
            priceManager.setPrice(taille, currentPrice * (1 - percentage / 100));
        }
    }

    // Méthodes privées
    private void verifPizza(PizzaRequestDto pizzaRequestDto) {
        if (pizzaRequestDto == null) {
            throw new PizzaException("La pizza doit exister");
        }
        if (pizzaRequestDto.nom() == null || pizzaRequestDto.nom().isBlank()) {
            throw new PizzaException("Le nom de la pizza ne peut pas être nul");
        }
        if (pizzaRequestDto.taille() == null) {
            throw new PizzaException("Vous devez renseigner une taille valide pour la pizza");
        }
        if (pizzaRequestDto.ingredients() == null || pizzaRequestDto.ingredients().isEmpty()) {
            throw new PizzaException("La liste des ingrédients de la pizza est obligatoire");
        }
        if (pizzaRequestDto.tarif() == null || pizzaRequestDto.tarif() < 0) {
            throw new PizzaException("Le tarif de la pizza doit être renseigné et positif");
        }
    }

    private void remplacer(Pizza pizza, Pizza pizzaExistant) {
        if (pizza.getNom() != null && !pizza.getNom().isBlank())
            pizzaExistant.setNom(pizza.getNom());
        if (pizza.getTaille() != null)
            pizzaExistant.setTaille(pizza.getTaille());
        if (pizza.getTarif() != null)
            pizzaExistant.setTarif(pizza.getTarif());
        if (pizza.getIngredients() != null)
            pizzaExistant.setIngredients(pizza.getIngredients());
    }

    private List<Pizza> recherches(Integer id, String nom, Ingredient ingredient, List<Pizza> liste) {
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