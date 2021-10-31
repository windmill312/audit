package com.windmill312.audit.integration.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.nio.file.Paths;

@TestConfiguration
@Slf4j
public
class DatabaseConfig {
    private final static String POSTGRES_LATEST_VERSION = "postgres:latest";
    private final static String SUPERUSER_LOGIN = "postgres";
    private final static String SUPERUSER_PASSWORD = "postgres:latest";
    private final static String DATABASE_NAME = "audit";
    private final static String SCHEMA_NAME = "audit";
    private final static Integer POSTGRES_PORT = 5432;

    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        final var postgresContainer = new PostgreSQLContainer<>(POSTGRES_LATEST_VERSION)
                .withUsername(SUPERUSER_LOGIN)
                .withPassword(SUPERUSER_PASSWORD)
                .withDatabaseName(DATABASE_NAME)
                .withExposedPorts(POSTGRES_PORT)
                .withCopyFileToContainer(
                        MountableFile.forHostPath(
                                Paths.get(".", "/init/init.sql")
                                        .normalize()
                                        .toAbsolutePath()
                        ),
                        "/docker-entrypoint-initdb.d/"
                );

        postgresContainer.start();
        return postgresContainer;
    }

    @Bean
    @Primary
    public DataSource dataSource(PostgreSQLContainer container) {
        final var hikariConfig = new HikariConfig();
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setDriverClassName(container.getDriverClassName());
        hikariConfig.setSchema(SCHEMA_NAME);
        return new HikariDataSource(hikariConfig);
    }
}