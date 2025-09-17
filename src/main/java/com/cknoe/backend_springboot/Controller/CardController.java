package com.cknoe.backend_springboot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.cknoe.backend_springboot.dto.CardDTO;
import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.service.CardService;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    List<CardDTO> getMyCards(@AuthenticationPrincipal UserDetails userDetails) {
        return cardService.getCardsForUser(userDetails.getUsername());
    }

    @PostMapping
    ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        CardDTO created = cardService.createCard(cardDTO, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    CardDTO getMyCard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return cardService.getCard(id, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    CardDTO updateMyCard(@PathVariable Long id,
            @Valid @RequestBody CardDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return cardService.updateCard(id, dto, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    void deleteMyCard(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        cardService.deleteCard(id, userDetails.getUsername());
    }

    // ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    List<CardDTO> allCards() {
        return cardService.getAllCards();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    Card newCard(@RequestBody Card newCard) {
        return cardService.createCardAdmin(newCard);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    CardDTO replaceCard(@PathVariable Long id, @RequestBody Card newCard) {
        return cardService.replaceCardAdmin(id, newCard);
    }
}
