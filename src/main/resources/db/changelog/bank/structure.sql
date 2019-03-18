--liquibase formatted sql

--changeset leonis:b1.0.4

CREATE TABLE Bank.Raws (
    Id BIGINT IDENTITY PRIMARY KEY,
    Raw TEXT NOT NULL
);