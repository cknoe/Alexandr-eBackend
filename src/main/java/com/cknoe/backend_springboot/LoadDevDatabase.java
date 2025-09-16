package com.cknoe.backend_springboot;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.entity.CardCollection;
import com.cknoe.backend_springboot.repository.CardRepository;
import com.cknoe.backend_springboot.repository.AppUserRepository;
import com.cknoe.backend_springboot.repository.CardCollectionRepository;

@Configuration
@Profile("dev")
public class LoadDevDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDevDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CardRepository cardRepository, AppUserRepository appUserRepository,
            CardCollectionRepository cardCollectionRepository) {
        return args -> {
            log.info("Cleaning up cards table");
            cardRepository.deleteAll();
            log.info("Cleaning up card_collection table");
            cardCollectionRepository.deleteAll();

            // Users

            // Collections
            ArrayList<CardCollection> cardCollectionList = new ArrayList<CardCollection>();

            CardCollection cardCollection1 = new CardCollection();
            cardCollection1.setName("Collection 1");
            cardCollection1.setOwner(appUserRepository.findById(1L).orElse(null));
            cardCollectionList.add(cardCollection1);

            CardCollection cardCollection2 = new CardCollection();
            cardCollection2.setName("Collection 1");
            cardCollection2.setOwner(appUserRepository.findById(1L).orElse(null));
            cardCollectionList.add(cardCollection2);

            cardCollectionList.forEach((cardCollection -> {
                log.info("Preloading : " + cardCollectionRepository.save(cardCollection));
            }));

            // Cards
            ArrayList<Card> cardList = new ArrayList<Card>();

            Card cardWebPage = new Card();
            cardWebPage.setTitle("Carte avec contenu");
            cardWebPage.setDescription("Contenu normal");
            cardWebPage.setContent("https://react.dev/");
            cardWebPage.setOwner(appUserRepository.findById(1L).orElse(null));
            cardWebPage.setCollection(cardCollection1);
            cardList.add(cardWebPage);

            Card cardVideoPage = new Card();
            cardVideoPage.setTitle("Carte avec contenu");
            cardVideoPage.setDescription("Contenu vidéo");
            cardVideoPage.setContent("https://www.youtube.com/watch?v=w7ejDZ8SWv8");
            cardVideoPage.setOwner(appUserRepository.findById(1L).orElse(null));
            cardVideoPage.setCollection(cardCollection1);
            cardList.add(cardVideoPage);

            Card cardNoContent = new Card();
            cardNoContent.setTitle("Carte sans contenu");
            cardNoContent.setDescription("Description");
            cardNoContent.setOwner(appUserRepository.findById(1L).orElse(null));
            cardNoContent.setCollection(cardCollection2);
            cardList.add(cardNoContent);

            cardList.forEach(card -> {
                log.info("Preloading " + cardRepository.save(card));
            });
        };
    }
}
