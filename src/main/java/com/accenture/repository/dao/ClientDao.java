package com.accenture.repository.dao;


import com.accenture.repository.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientDao extends JpaRepository<Client, String> {

}
