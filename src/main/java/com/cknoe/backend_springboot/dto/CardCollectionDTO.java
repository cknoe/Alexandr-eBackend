package com.cknoe.backend_springboot.dto;

import java.util.ArrayList;
import java.util.List;

import com.cknoe.backend_springboot.dto.userDTOs.UserDTO;
import com.cknoe.backend_springboot.entity.CardCollection;

import jakarta.validation.constraints.NotBlank;

public record CardCollectionDTO(
        Long id,
        @NotBlank String name,
        List<CardDTO> cards,
        UserDTO owner) {

    public static CardCollectionDTO fromEntity(CardCollection cardCollection) {
        List<CardDTO> cardDTOList = new ArrayList<CardDTO>();
        cardCollection.getCards().forEach(card -> {
            cardDTOList.add(CardDTO.fromEntity(card));
        });
        return new CardCollectionDTO(cardCollection.getId(), cardCollection.getName(), cardDTOList,
                UserDTO.fromEntity(cardCollection.getOwner()));
    }

    public static CardCollection toEntity(CardCollectionDTO dto) {
        CardCollection cardCollection = new CardCollection();
        cardCollection.setName(dto.name());
        return cardCollection;
    }
}
