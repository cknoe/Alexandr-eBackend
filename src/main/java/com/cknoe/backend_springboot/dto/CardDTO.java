package com.cknoe.backend_springboot.dto;

import org.hibernate.validator.constraints.URL;

import com.cknoe.backend_springboot.entity.Card;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardDTO(
        Long id,
        @NotBlank String title,
        @NotBlank String description,
        @URL(message = "Content must be a valid URL") String content,
        String ownerUsername,
        @NotNull Long collectionId,
        String collectionName) {

    public static CardDTO fromEntity(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getContent(),
                card.getOwner().getUsername(),
                card.getCollection().getId(),
                card.getCollection().getName());
    }

    public static Card toEntity(CardDTO cardDTO) {
        Card card = new Card();
        card.setTitle(cardDTO.title());
        card.setDescription(cardDTO.description());
        card.setContent(cardDTO.content());
        return card;
    }
}