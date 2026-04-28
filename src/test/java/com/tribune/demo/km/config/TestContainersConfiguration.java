package com.tribune.demo.km.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * TestContainers Configuration for MongoDB
 * <p>
 * This configuration class sets up a MongoDB container for testing purposes.
 * The container is started automatically when tests run and is cleaned up afterwards.
 * <p>
 * Benefits:
 * - Uses real MongoDB container (not embedded)
 * - More realistic testing environment
 * - Easy cleanup between tests
 * - No need for external MongoDB server during testing
 */
@Slf4j
@TestConfiguration
public class TestContainersConfiguration {

    /**
     * Creates and starts a MongoDB container for testing
     * <p>
     * The container will:
     * - Start before tests begin
     * - Be available for all tests
     * - Stop and be removed after tests complete
     *
     * @return MongoDBContainer instance
     */
    @Bean
    public MongoDBContainer mongoDBContainer() {
        log.info("Starting MongoDB TestContainer...");

        MongoDBContainer mongoDBContainer = new MongoDBContainer(
                DockerImageName.parse("mongo:7.0.2")
        )
                .withReuse(false);  // Set to true if you want to reuse containers between test runs

        mongoDBContainer.start();

        log.info("MongoDB TestContainer started on: {}", mongoDBContainer.getReplicaSetUrl());

        return mongoDBContainer;
    }
}

