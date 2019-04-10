--liquibase formatted sql

--changeset leonis:dsl:1.0.2

CREATE TABLE Bank.Raw (
    Id BIGINT IDENTITY PRIMARY KEY,
    Word NVARCHAR(255) NOT NULL,
    Raw TEXT NOT NULL
);
