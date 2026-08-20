CREATE TABLE IF NOT EXISTS course (
    id      UUID PRIMARY KEY,
    ref     VARCHAR(255),
    title   VARCHAR(255),
    credits INTEGER NOT NULL
);
