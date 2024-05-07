-- liquibase formatted sql

-- changeset ryabchuk:4
INSERT INTO person (id, name, age, password, role) VALUES
                                                       (2, 'Alice Johnson', 28, 'alicepass', 'ROLE_USER'),
                                                       (3, 'Bob Brown', 35, 'bobpass', 'ROLE_USER'),
                                                       (4, 'Charlie Davis', 40, 'charliepass', 'ROLE_USER'),
                                                       (5, 'David Miller', 32, 'davidpass', 'ROLE_USER'),
                                                       (6, 'Eve Smith', 29, 'evepass', 'ROLE_USER'),
                                                       (7, 'Frank Johnson', 38, 'frankpass', 'ROLE_USER'),
                                                       (8, 'Grace Brown', 45, 'gracepass', 'ROLE_USER'),
                                                       (9, 'Helen Davis', 50, 'helenpass', 'ROLE_USER'),
                                                       (10, 'Ian Miller', 42, 'ianpass', 'ROLE_USER'),
                                                       (11, 'Jack Smith', 39, 'jackpass', 'ROLE_USER');