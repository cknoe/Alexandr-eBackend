package com.cknoe.backend_springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cknoe.backend_springboot.entity.CardCollection;

public interface CardCollectionRepository extends JpaRepository<CardCollection, Long> {

}
