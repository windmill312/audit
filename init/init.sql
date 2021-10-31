CREATE USER audit WITH PASSWORD 'audit';

--workaround for 'create database if not exists'
CREATE EXTENSION dblink;
DO
$do$
    BEGIN
        IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'audit') THEN
            PERFORM dblink_exec(
                'dbname=' || current_database(),
                'CREATE DATABASE audit'
            );
        END IF;
    END
$do$;

GRANT ALL PRIVILEGES ON DATABASE audit TO audit;

\connect audit;

CREATE SCHEMA audit AUTHORIZATION audit;