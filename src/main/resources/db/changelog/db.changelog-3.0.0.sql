-- liquibase formatted sql

-- changeset ryabchuk:3
CREATE TABLE IF NOT EXISTS house_residents (
    id SERIAL PRIMARY KEY,
    house_id BIGINT REFERENCES house(id) ON UPDATE CASCADE ON DELETE CASCADE,
    user_id  BIGINT REFERENCES person(id) ON UPDATE CASCADE ON DELETE CASCADE
    );
-- rollback DROP TABLE house_residents;