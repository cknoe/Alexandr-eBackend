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

import com.cknoe.backend_springboot.entity.AppUser;
import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.exception.CardNotFoundException;
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

    @GetMapping("/admincards")
    List<Card> all() {
        return cardRepository.findAll();
    }

    @GetMapping("/cards")
    List<Card> myCards(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return cardRepository.findByOwnerUsername(username);
    }

    @PostMapping("/admincards")
    Card newCard(@RequestBody Card newCard) {
        return cardRepository.save(newCard);
    }

    @PostMapping("/cards")
    public ResponseEntity<Card> createCard(@RequestBody Card card, @AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = appUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        card.setOwner(user);
        return ResponseEntity.ok(cardRepository.save(card));
    }

    @GetMapping("/cards/{id}")
    Card one(@PathVariable Long id) {
        return cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    @PutMapping("/cards/{id}")
    Card replaceCard(@RequestBody Card newCard, @PathVariable Long id) {
        return cardRepository.findById(id)
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
    }

    @DeleteMapping("/cards/{id}")
    void deleteCard(@PathVariable Long id) {
        cardRepository.deleteById(id);
    }

}
