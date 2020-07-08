package org.devzone.addresses.config;

import io.r2dbc.spi.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.*;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.connectionfactory.init.ScriptException;

@Configuration(proxyBeanMethods = false)
public class DatabaseInitializationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializationConfiguration.class);

    @Autowired
    void initializeDatabase(ConnectionFactory connectionFactory) {
        logger.info("Initialize database");
        try {
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource[] scripts = new Resource[] { resourceLoader.getResource("classpath:/db/createTable.sql"),
                    resourceLoader.getResource("classpath:/db/inserts.sql") };
            new ResourceDatabasePopulator(scripts).execute(connectionFactory).block();
        } catch (ScriptException e) {
            logger.error("Cannot initialize database");
        }
    }

}