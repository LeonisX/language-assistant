--liquibase formatted sql

--changeset leonis:bank:1.0.2

CREATE TABLE Bank.Raws (
    Id BIGINT IDENTITY PRIMARY KEY,
    Raw TEXT NOT NULL
);