package com.cknoe.backend_springboot.dto;

import com.cknoe.backend_springboot.entity.Card;

public record CardDTO(
        Long id,
        String title,
        String description,
        String content,
        String ownerUsername) {
    public static CardDTO fromEntity(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getContent(),
                card.getOwner().getUsername());
    }
}