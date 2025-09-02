package com.cknoe.backend_springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(CardRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Card("Title 1", "Description 1", "Content 1")));
            log.info("Preloading " + repository.save(new Card("Title 2", "Description 2", "Content 2")));
        };
    }
}
