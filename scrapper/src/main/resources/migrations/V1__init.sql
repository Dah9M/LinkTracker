-- liquibase formatted sql

-- changeset chat_user:001
CREATE TABLE IF NOT EXISTS chat_user
(
    id            BIGSERIAL PRIMARY KEY,
    chat_id       VARCHAR(255) NOT NULL UNIQUE,
    registered_at TIMESTAMP DEFAULT NOW()
);

-- changeset link:002
CREATE TABLE IF NOT EXISTS link
(
    id            BIGSERIAL PRIMARY KEY,
    url           TEXT NOT NULL UNIQUE,
    resource_type VARCHAR(50),
    created_at    TIMESTAMP DEFAULT NOW(),
    updated_at    TIMESTAMP DEFAULT NOW(),
    last_checked_at TIMESTAMP DEFAULT NOW()
);

-- changeset subscription:003
CREATE TABLE IF NOT EXISTS subscription
(
    user_id       BIGINT NOT NULL REFERENCES chat_user (id) ON DELETE CASCADE,
    link_id       BIGINT NOT NULL REFERENCES link (id) ON DELETE CASCADE,
    created_at    TIMESTAMP DEFAULT NOW(),
    PRIMARY KEY (user_id, link_id) -- Составной ключ вместо отдельного id
);