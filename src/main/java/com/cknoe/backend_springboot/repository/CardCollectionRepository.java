package com.cknoe.backend_springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cknoe.backend_springboot.entity.CardCollection;

public interface CardCollectionRepository extends JpaRepository<CardCollection, Long> {
    List<CardCollection> findByOwnerUsername(String Username);
}
