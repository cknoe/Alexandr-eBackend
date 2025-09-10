package com.cknoe.backend_springboot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.cknoe.backend_springboot.dto.CardDTO;
import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.exception.CardNotFoundException;
import com.cknoe.backend_springboot.exception.ForbiddenException;
import com.cknoe.backend_springboot.repository.CardRepository;
import com.cknoe.backend_springboot.repository.AppUserRepository;

@RestController
public class CardController {

    private final CardRepository cardRepository;
    private final AppUserRepository appUserRepository;

    public CardController(CardRepository cardRepository, AppUserRepository appUserRepository) {
        this.cardRepository = cardRepository;
        this.appUserRepository = appUserRepository;
    }

    @GetMapping("/cards")
    List<CardDTO> getMyCards(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return cardRepository.findByOwnerUsername(username).stream().map(CardDTO::fromEntity).toList();
    }

    @PostMapping("/cards")
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Card card = CardDTO.toEntity(cardDTO);
        card.setOwner(user);
        return ResponseEntity.ok(CardDTO.fromEntity(cardRepository.save(card)));
    }

    @GetMapping("/cards/{id}")
    CardDTO getMyCard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        if (card.getOwner().getUsername() != userDetails.getUsername()) {
            throw new ForbiddenException();
        }
        return CardDTO.fromEntity(card);
    }

    @PutMapping("/cards/{id}")
    CardDTO replaceMyCard(@Valid @RequestBody CardDTO newCardDTO, @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Card newCard = CardDTO.toEntity(newCardDTO);
        Card updatedCard = cardRepository.findById(id)
                .map(card -> {
                    if (!card.getOwner().getUsername().equals(userDetails.getUsername())) {
                        throw new ForbiddenException();
                    }
                    card.setTitle(newCard.getTitle());
                    card.setDescription(newCard.getDescription());
                    card.setContent(newCard.getContent());
                    return cardRepository.save(card);
                })
                .orElseThrow(() -> new CardNotFoundException(id));
        ;
        return CardDTO.fromEntity(updatedCard);
    }

    @DeleteMapping("/cards/{id}")
    void deleteMyCard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        if (!card.getOwner().getUsername().equals(userDetails.getUsername())) {
            throw new ForbiddenException();
        }
        cardRepository.deleteById(id);
    }

    @GetMapping("/admin/cards")
    List<CardDTO> all() {
        return cardRepository.findAll().stream().map(CardDTO::fromEntity).toList();
    }

    @PostMapping("/admin/cards")
    Card newCard(@RequestBody Card newCard) {
        return cardRepository.save(newCard);
    }

    @PutMapping("/admin/cards/{id}")
    CardDTO replaceCard(@RequestBody Card newCard, @PathVariable Long id) {
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
