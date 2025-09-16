package com.cknoe.backend_springboot;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cknoe.backend_springboot.entity.AppUser;
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
            log.info("Cleaning up app_user table");
            appUserRepository.deleteAll();

            // Users
            ArrayList<AppUser> userList = new ArrayList<AppUser>();
            String password = "$2a$12$2Yr7KGSTam9SuSjzqmxuUu5/.Vx6QKxRVonZSfjwYqi0UlM.0scd."; // password

            AppUser userAdmin = new AppUser();
            userAdmin.setUsername("Admin");
            userAdmin.setRole("Admin");
            userAdmin.setPassword(password);
            userList.add(userAdmin);

            AppUser userNormal = new AppUser();
            userNormal.setUsername("TestUser");
            userNormal.setRole("USER");
            userNormal.setPassword(password);
            userList.add(userNormal);

            userList.forEach(user -> {
                appUserRepository.save(user);
            });

            // Collections
            ArrayList<CardCollection> cardCollectionList = new ArrayList<CardCollection>();

            CardCollection cardCollection1 = new CardCollection();
            cardCollection1.setName("Collection 1");
            cardCollection1.setOwner(userAdmin);
            cardCollectionList.add(cardCollection1);

            CardCollection cardCollection2 = new CardCollection();
            cardCollection2.setName("Collection 1");
            cardCollection2.setOwner(userAdmin);
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
            cardWebPage.setOwner(userAdmin);
            cardWebPage.setCollection(cardCollection1);
            cardList.add(cardWebPage);

            Card cardVideoPage = new Card();
            cardVideoPage.setTitle("Carte avec contenu");
            cardVideoPage.setDescription("Contenu vidéo");
            cardVideoPage.setContent("https://www.youtube.com/watch?v=w7ejDZ8SWv8");
            cardVideoPage.setOwner(userAdmin);
            cardVideoPage.setCollection(cardCollection1);
            cardList.add(cardVideoPage);

            Card cardNoContent = new Card();
            cardNoContent.setTitle("Carte sans contenu");
            cardNoContent.setDescription("Description");
            cardNoContent.setOwner(userAdmin);
            cardNoContent.setCollection(cardCollection2);
            cardList.add(cardNoContent);

            cardList.forEach(card -> {
                log.info("Preloading " + cardRepository.save(card));
            });
        };
    }
}
