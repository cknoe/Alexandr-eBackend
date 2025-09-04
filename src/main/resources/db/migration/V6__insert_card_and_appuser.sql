INSERT INTO
    app_user (username, password, role)
VALUES (
        'testuser2',
        '$2a$12$2Yr7KGSTam9SuSjzqmxuUu5/.Vx6QKxRVonZSfjwYqi0UlM.0scd.', -- mot de passe = "password"
        'USER'
    );

INSERT INTO
    card (
        title,
        description,
        content,
        user_id
    )
VALUES (
        'User2 Card 1',
        'Description for user2 card 1',
        'Content for user2 card 1',
        (
            SELECT id
            FROM app_user
            WHERE
                username = 'testuser2'
        )
    ),
    (
        'User2 Card 2',
        'Description for user2 card 2',
        'Content for user2 card 2',
        (
            SELECT id
            FROM app_user
            WHERE
                username = 'testuser2'
        )
    );