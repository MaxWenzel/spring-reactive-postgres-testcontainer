package org.devzone.testing;

import org.devzone.addresses.model.Address;
import org.devzone.addresses.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
class TestcontainersTests {

    private static final Logger logger = LoggerFactory.getLogger(TestcontainersTests.class);

    @Autowired
    private AddressService addressService;

     // will be shared between test methods
    @Container
    private static final PostgreSQLContainer container = new PostgreSQLContainer()
             .withDatabaseName("springboot")
             .withUsername("springboot")
             .withPassword("secret");

    @BeforeEach
    public void setUp() {
        Long totalCount = addressService.getTotalCount().block(Duration.ofSeconds(1));
        if (totalCount == 0) {
            try {
                init();
            } catch (IOException e) {
               logger.error("Cannot insert address", e);
            }
        }
    }

    public void init() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:postalcode_locality_de.csv");

        try ( BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream())) ) {
            List<String> addresses = reader.lines().collect(Collectors.toList());
            for (String plainAddress : addresses) {
                String[] fields = plainAddress.split(",");
                Address address = new Address();
                address.setPostalCode(fields[2]);
                address.setLocality(fields[1]);
                address.setState(fields[3]);
                Mono<Address> result = addressService.saveAddress(address);
                result.subscribe(l -> logger.debug("Created entry with index {}", l));
            }
        }
        logger.info("Initialized Postgres");
    }

    @Test
    void testDatabaseConnection() {
        assertTrue(container.isRunning());
        logger.info(container.getDatabaseName());
        logger.info(container.getDriverClassName());
        logger.info(container.getJdbcUrl());
    }

    @Test
    void findAll() {
        Flux<Address> allAddresses = addressService.findAll();
        List<Address> addresses = allAddresses.collectList().block(Duration.ofSeconds(10));
        assertThat(addresses).hasSize(12935);
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> container.getJdbcUrl().replace("jdbc:", "r2dbc:"));
        registry.add("spring.r2dbc.username", () -> container.getUsername());
        registry.add("spring.r2dbc.password", () -> container.getPassword());
    }

}