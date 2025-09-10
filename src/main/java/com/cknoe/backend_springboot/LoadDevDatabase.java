package com.cknoe.backend_springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.repository.CardRepository;
import com.cknoe.backend_springboot.repository.AppUserRepository;

@Configuration
@Profile("dev")
public class LoadDevDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDevDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CardRepository repository, AppUserRepository appUserRepository) {
        return args -> {
            log.info("Cleaning up cards table");
            repository.deleteAll();
            log.info("Preloading " + repository.save(
                    new Card("Carte avec contenu", "Contenu normal", "https://react.dev/",
                            appUserRepository.findById(1L).orElse(null))));
            log.info("Preloading " + repository.save(
                    new Card("Carte avec contenu", "Contenu vidéo", "https://www.youtube.com/watch?v=w7ejDZ8SWv8",
                            appUserRepository.findById(1L).orElse(null))));
            log.info("Preloading " + repository.save(
                    new Card("Carte sans contenu", "Description", appUserRepository.findById(1L).orElse(null))));
        };
    }
}
