CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    content VARCHAR(200) NOT NULL,
);