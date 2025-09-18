package com.cknoe.backend_springboot.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cknoe.backend_springboot.dto.CardCollectionDTO;
import com.cknoe.backend_springboot.service.CardCollectionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/collections")
public class CardCollectionController {

    private final CardCollectionService cardCollectionService;

    public CardCollectionController(CardCollectionService cardCollectionService) {
        this.cardCollectionService = cardCollectionService;
    }

    @GetMapping
    public List<CardCollectionDTO> getCollections(@AuthenticationPrincipal UserDetails userDetails) {
        return cardCollectionService.getCollectionForUsername(userDetails.getUsername());
    }

    @GetMapping("/{id}")
    public CardCollectionDTO getCollectionById(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return cardCollectionService.getCollectionById(id, userDetails.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardCollectionDTO createCardCollection(@Valid @RequestBody CardCollectionDTO cardCollectionDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        return cardCollectionService.createCardCollection(cardCollectionDTO, userDetails.getUsername());
    }

    @PutMapping("/{id}")
    public CardCollectionDTO updateCardCollection(@PathVariable Long id,
            @Valid @RequestBody CardCollectionDTO cardCollectionDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        return cardCollectionService.updateCardCollection(id, cardCollectionDTO, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCollection(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        cardCollectionService.deleteCardCollection(id, userDetails.getUsername());
    }
}
