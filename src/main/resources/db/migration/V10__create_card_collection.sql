CREATE TABLE card_collection (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    creation_date TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_cardcollection_user FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
);

ALTER TABLE card ADD COLUMN collection_id BIGINT;

ALTER TABLE card
ADD CONSTRAINT fk_card_collection FOREIGN KEY (collection_id) REFERENCES card_collection (id) ON DELETE CASCADE;