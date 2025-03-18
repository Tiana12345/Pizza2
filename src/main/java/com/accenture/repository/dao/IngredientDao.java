package com.accenture.repository.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.accenture.repository.entity.Ingredient;

public interface IngredientDao extends JpaRepository<Ingredient, Integer> {
}
