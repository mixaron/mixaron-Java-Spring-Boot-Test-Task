-- liquibase formatted sql

-- changeset ryabchuk:2
CREATE TABLE IF NOT EXISTS house (
    id SERIAL PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    owner_id BIGINT UNIQUE,
    CONSTRAINT fk_house_owner FOREIGN KEY (owner_id) REFERENCES person(id) ON UPDATE CASCADE ON DELETE SET NULL
    );
-- rollback DROP TABLE house;