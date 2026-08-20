CREATE TABLE IF NOT EXISTS user_hei (
    id            UUID PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(255) NOT NULL UNIQUE,
    phone_number  VARCHAR(255) NOT NULL UNIQUE,
    address       VARCHAR(255),
    password      VARCHAR(255),
    role          VARCHAR(255) NOT NULL
                  CHECK (role IN ('ADMIN','STUDENT','TEACHER'))
);
