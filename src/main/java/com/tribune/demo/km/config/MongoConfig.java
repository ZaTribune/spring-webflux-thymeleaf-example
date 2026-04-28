package com.tribune.demo.km.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Collections;

@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.tribune.demo.km.data.repository")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.host:localhost}")
    private String host;

    @Value("${spring.data.mongodb.port:27017}")
    private int port;

    @Value("${spring.data.mongodb.database:kitchen_master}")
    private String database;

    @Value("${spring.data.mongodb.username:km_user}")
    private String username;

    @Value("${spring.data.mongodb.password:Pass1234}")
    private String password;

    @Value("${spring.data.mongodb.authentication-database:#{null}}")
    private String authenticationDatabase;

    @Override
    protected @NonNull String getDatabaseName() {
        return database;
    }

    @Override
    @Bean
    public @NonNull MongoClient reactiveMongoClient() {
        // Auth source: use authentication-database if set, otherwise use the target database
        String authSource = (authenticationDatabase != null && !authenticationDatabase.isEmpty())
                ? authenticationDatabase
                : database;

        log.info("═══════════════════════════════════");
        log.info("MongoDB Connection Configuration");
        log.info("═══════════════════════════════════");
        log.info("- Host: {}", host);
        log.info("- Port: {}", port);
        log.info("- Database: {}", database);
        log.info("- Username: {}", username);
        log.info("- Password: {}", password != null && !password.isEmpty());
        log.info("- Auth Source: {}", authSource);
        log.info("═══════════════════════════════════");

        MongoCredential credential = MongoCredential.createCredential(
                username,
                authSource,
                password.toCharArray()
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyToClusterSettings(builder ->
                        builder.hosts(Collections.singletonList(new ServerAddress(host, port)))
                )
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }
}

