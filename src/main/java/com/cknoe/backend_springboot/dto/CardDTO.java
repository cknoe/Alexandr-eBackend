package com.cknoe.backend_springboot.dto;

import org.hibernate.validator.constraints.URL;

import com.cknoe.backend_springboot.entity.Card;

import jakarta.validation.constraints.NotBlank;

public record CardDTO(
        Long id,
        @NotBlank String title,
        @NotBlank String description,
        @URL(message = "Content must be a valid URL") String content,
        String ownerUsername) {

    public static CardDTO fromEntity(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getContent(),
                card.getOwner().getUsername());
    }

    public static Card toEntity(CardDTO cardDTO) {
        Card card = new Card();
        card.setTitle(cardDTO.title());
        card.setDescription(cardDTO.description());
        card.setContent(cardDTO.content());
        return card;
    }
}