package com.windmill312.audit.integration;

import com.windmill312.audit.integration.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = DatabaseConfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class IntegrationTests {

    @Test
    public void contextLoads() {
    }
}