package com.cknoe.backend_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cknoe.backend_springboot.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

}