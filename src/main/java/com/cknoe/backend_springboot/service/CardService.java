package com.cknoe.backend_springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cknoe.backend_springboot.repository.CardRepository;
import com.cknoe.backend_springboot.repository.CardCollectionRepository;
import com.cknoe.backend_springboot.dto.CardDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.entity.CardCollection;
import com.cknoe.backend_springboot.exception.CardNotFoundException;
import com.cknoe.backend_springboot.exception.CollectionNotFoundException;
import com.cknoe.backend_springboot.exception.ForbiddenException;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AppUserService appUserService;
    private final CardCollectionRepository cardCollectionRepository;

    public CardService(CardRepository cardRepository,
            CardCollectionRepository cardCollectionRepository,
            AppUserService appUserService) {
        this.cardRepository = cardRepository;
        this.cardCollectionRepository = cardCollectionRepository;
        this.appUserService = appUserService;
    }

    public List<CardDTO> getCardsForUser(String username) {
        return cardRepository.findByOwnerUsernameOrderByCreationDateAsc(username)
                .stream()
                .map(CardDTO::fromEntity)
                .toList();
    }

    @Transactional
    public CardDTO createCard(CardDTO dto, String username) {
        AppUser user = appUserService.getCurrentUser(username);

        CardCollection collection = cardCollectionRepository.findById(dto.collectionId())
                .orElseThrow(() -> new CollectionNotFoundException(dto.collectionId()));
        if (!collection.getOwner().getId().equals(user.getId())) {
            throw new ForbiddenException();
        }

        Card card = CardDTO.toEntity(dto);
        card.setOwner(user);
        card.setCollection(collection);
        return CardDTO.fromEntity(cardRepository.save(card));
    }

    public CardDTO getCard(Long id, String username) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        if (!card.getOwner().getUsername().equals(username)) {
            throw new ForbiddenException();
        }
        return CardDTO.fromEntity(card);
    }

    @Transactional
    public CardDTO updateCard(Long id, CardDTO dto, String username) {

        AppUser user = appUserService.getCurrentUser(username);

        Card newCardData = CardDTO.toEntity(dto);

        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        if (!card.getOwner().getUsername().equals(username)) {
            throw new ForbiddenException();
        }

        if (!card.getCollection().getId().equals(dto.collectionId())) {
            CardCollection newCollection = cardCollectionRepository.findById(dto.collectionId())
                    .orElseThrow(() -> new CollectionNotFoundException(id));
            if (!newCollection.getOwner().getId().equals(user.getId())) {
                throw new ForbiddenException();
            }
            card.setCollection(newCollection);
        }

        card.setTitle(newCardData.getTitle());
        card.setDescription(newCardData.getDescription());
        card.setContent(newCardData.getContent());
        return CardDTO.fromEntity(cardRepository.save(card));
    }

    public void deleteCard(Long id, String username) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        if (!card.getOwner().getUsername().equals(username)) {
            throw new ForbiddenException();
        }

        cardRepository.delete(card);
    }

    // ADMIN

    public List<CardDTO> getAllCards() {
        return cardRepository.findAll().stream()
                .map(CardDTO::fromEntity)
                .toList();
    }

    public Card createCardAdmin(Card newCard) {
        return cardRepository.save(newCard);
    }

    public CardDTO replaceCardAdmin(Long id, Card newCard) {
        Card updatedCard = cardRepository.findById(id)
                .map(card -> {
                    card.setTitle(newCard.getTitle());
                    card.setDescription(newCard.getDescription());
                    card.setContent(newCard.getContent());
                    return cardRepository.save(card);
                })
                .orElseGet(() -> {
                    newCard.setId(id);
                    return cardRepository.save(newCard);
                });
        return CardDTO.fromEntity(updatedCard);
    }
}
