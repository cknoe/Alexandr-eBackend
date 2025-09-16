package com.cknoe.backend_springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.cknoe.backend_springboot.entity.Card;
import com.cknoe.backend_springboot.repository.CardRepository;

@Configuration
@Profile("test")
public class LoadTestDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadTestDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CardRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Card()));
            log.info("Preloading " + repository.save(new Card()));
        };
    }
}
