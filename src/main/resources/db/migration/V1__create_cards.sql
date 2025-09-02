CREATE SEQUENCE IF NOT EXISTS card_id_seq START
WITH
    1 INCREMENT BY 1;

CREATE TABLE card (
    id BIGINT PRIMARY KEY DEFAULT nextval('card_id_seq'),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    content VARCHAR(255)
);