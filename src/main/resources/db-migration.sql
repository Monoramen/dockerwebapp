CREATE  TABLE users (
                        id SERIAL PRIMARY KEY,
                        username VARCHAR(255) UNIQUE,
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        about VARCHAR(255),
                        password VARCHAR(255) NOT NULL
);

CREATE TABLE chats(
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL
);

CREATE TABLE messages (
                          id SERIAL PRIMARY KEY,
                          text VARCHAR(255) NOT NULL,
                          date_time TIMESTAMP NOT NULL,
                          sender_id BIGINT,
                          chat_id BIGINT,
                          FOREIGN KEY (sender_id) REFERENCES users(id),
                          FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE
);

CREATE TABLE chat_participants (
                                   chat_id BIGINT,
                                   user_id BIGINT,
                                   PRIMARY KEY (chat_id, user_id),
                                   FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
                                   FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (username, first_name, last_name, about, password)
VALUES
    ('first', 'Jane', 'Smith', 'Another bio', '1234'),
    ('second', 'Michael', 'Johnson', 'Yet another bio', '1234');

INSERT INTO chats (name) VALUES
    ('chat1'),
    ('chat2');

INSERT INTO chat_participants (chat_id, user_id)
VALUES
    (1, 1),  -- Пользователь с id=1 добавлен в чат с id=1
    (1, 2),  -- Пользователь с id=2 добавлен в чат с id=1
    (2, 1),  -- Пользователь с id=1 добавлен в чат с id=2
    (2, 2);  -- Пользователь с id=2 добавлен в чат с id=2


INSERT INTO messages (text, date_time, sender_id, chat_id)
VALUES
    ('Hello, this is Jane', NOW(), 1, 1),  -- Сообщение от Jane в чат1
    ('Hi Jane, this is Michael', NOW(), 2, 1),  -- Сообщение от Michael в чат1
    ('What’s up?', NOW(), 1, 2),  -- Сообщение от Jane в чат2
    ('Not much, just working', NOW(), 2, 2);  -- Сообщение от Michael в чат2
