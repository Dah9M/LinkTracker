-- Создание таблицы app_user
CREATE TABLE IF NOT EXISTS app_user
(
    id            BIGSERIAL PRIMARY KEY,
    chat_id       VARCHAR(255) NOT NULL UNIQUE,
    registered_at TIMESTAMP DEFAULT NOW()
);

-- Создание таблицы link
CREATE TABLE IF NOT EXISTS link
(
    id              BIGSERIAL PRIMARY KEY,
    url             TEXT        NOT NULL,
    resource_type   VARCHAR(50) NOT NULL,
    last_checked_at TIMESTAMP,
    created_at      TIMESTAMP DEFAULT NOW(),
    updated_at      TIMESTAMP DEFAULT NOW()
);

-- Создание таблицы subscription
CREATE TABLE IF NOT EXISTS subscription
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES app_user (id),
    link_id    BIGINT NOT NULL REFERENCES link (id),
    created_at TIMESTAMP DEFAULT NOW()
);
