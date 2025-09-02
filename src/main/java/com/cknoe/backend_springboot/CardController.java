package com.cknoe.backend_springboot;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    private final CardRepository repository;

    public CardController(CardRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/cards")
    List<Card> all() {
        return repository.findAll();
    }

    @PostMapping("/cards")
    Card newCard(@RequestBody Card newCard) {
        return repository.save(newCard);
    }

    @GetMapping("/cards/{id}")
    Card one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
    }

    @PutMapping("/cards/{id}")
    Card replaceCard(@RequestBody Card newCard, @PathVariable Long id) {
        return repository.findById(id)
                .map(card -> {
                    card.setTitle(newCard.getTitle());
                    card.setDescription(newCard.getDescription());
                    card.setContent(newCard.getContent());
                    return repository.save(card);
                })
                .orElseGet(() -> {
                    newCard.setId(id);
                    return repository.save(newCard);
                });
    }

    @DeleteMapping("/cards/{id}")
    void deleteCard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
