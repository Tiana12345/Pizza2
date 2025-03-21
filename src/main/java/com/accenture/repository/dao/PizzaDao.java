package com.accenture.repository.dao;

import com.accenture.repository.entity.Pizza;
import com.accenture.service.dto.PizzaResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PizzaDao extends JpaRepository<Pizza, Integer> {
}
