package com.cknoe.backend_springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class LoadTestDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadTestDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CardRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Card("H2 card", "test profile", "Content 1")));
            log.info("Preloading " + repository.save(new Card("Title 2", "Description 2", "Content 2")));
        };
    }
}
