package com.cknoe.backend_springboot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cknoe.backend_springboot.dto.CardCollectionDTO;
import com.cknoe.backend_springboot.dto.CardDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.entity.CardCollection;
import com.cknoe.backend_springboot.exception.CollectionNotFoundException;
import com.cknoe.backend_springboot.exception.ForbiddenException;
import com.cknoe.backend_springboot.repository.CardCollectionRepository;

@Service
public class CardCollectionService {

    private final CardCollectionRepository cardCollectionRepository;
    private final AppUserService appUserService;

    public CardCollectionService(CardCollectionRepository cardCollectionRepository, AppUserService appUserService) {
        this.cardCollectionRepository = cardCollectionRepository;
        this.appUserService = appUserService;
    }

    public CardCollectionDTO getCollectionById(Long id, String username) {
        CardCollection cardCollection = cardCollectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException(id));

        if (!cardCollection.getOwner().getUsername().equals(username)) {
            throw new ForbiddenException();
        }
        return CardCollectionDTO.fromEntity(cardCollection);
    }

    public List<CardCollectionDTO> getCollectionForUsername(String username) {
        List<CardCollection> cardCollectionList = cardCollectionRepository.findByOwnerUsername(username);
        List<CardCollectionDTO> response = new ArrayList<CardCollectionDTO>();
        cardCollectionList.forEach(cardCollection -> {
            response.add(CardCollectionDTO.fromEntity(cardCollection));
        });
        return response;
    }

    @Transactional
    public CardCollectionDTO createCardCollection(CardCollectionDTO dto, String username) {
        AppUser user = appUserService.getCurrentUser(username);

        CardCollection cardCollection = new CardCollection();
        cardCollection.setName(dto.name());
        cardCollection.setOwner(user);
        List<Card> cardList = new ArrayList<Card>();
        if (dto.cards() != null) {
            dto.cards().forEach(card -> {
                cardList.add(CardDTO.toEntity(card));
            });
        }
        cardCollection.setCards(cardList);
        return CardCollectionDTO.fromEntity(cardCollectionRepository.save(cardCollection));
    }

    @Transactional
    public CardCollectionDTO updateCardCollection(Long id, CardCollectionDTO dto, String username) {

        CardCollection newCardCollection = CardCollectionDTO.toEntity(dto);

        CardCollection cardCollection = cardCollectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException(id));

        if (!cardCollection.getOwner().getUsername().equals(username)) {
            throw new ForbiddenException();
        }

        cardCollection.setName(newCardCollection.getName());

        return CardCollectionDTO.fromEntity(cardCollectionRepository.save(cardCollection));
    }

    public void deleteCardCollection(Long id, String username) {
        CardCollection cardCollection = cardCollectionRepository.findById(id)
                .orElseThrow(() -> new CollectionNotFoundException(id));

        if (!cardCollection.getOwner().getUsername().equals(username)) {
            throw new ForbiddenException();
        }

        cardCollectionRepository.delete(cardCollection);
    }
}
