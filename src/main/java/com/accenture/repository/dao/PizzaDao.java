package com.accenture.repository.dao;

import com.accenture.repository.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaDao extends JpaRepository<Pizza, Integer> {
}
