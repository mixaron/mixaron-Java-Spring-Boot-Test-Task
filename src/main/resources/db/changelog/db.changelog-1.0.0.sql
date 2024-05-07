-- liquibase formatted sql

-- changeset ryabchuk:1
CREATE TABLE IF NOT EXISTS person (
id       SERIAL PRIMARY KEY,
name     VARCHAR(255) NOT NULL,
age      INTEGER NOT NULL,
password VARCHAR(255) NOT NULL,
house_id BIGINT,
role     VARCHAR(255) NOT NULL
);
-- rollback DROP TABLE person;