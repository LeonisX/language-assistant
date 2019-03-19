--liquibase formatted sql

--changeset leonis:1.0.2

CREATE TABLE Test.WordPlaces (
    Word NVARCHAR(136) PRIMARY KEY,
    Place BIGINT NOT NULL
);

CREATE TABLE Test.WordFrequencies (
    Word NVARCHAR(136) PRIMARY KEY,
    Frequency BIGINT NOT NULL
);

CREATE TABLE Test.WordLevel (
    Id BIGINT IDENTITY PRIMARY KEY,
    Word NVARCHAR(136) NOT NULL,
    Level VARCHAR(3) NOT NULL,
    Percent INT,
    Definition TEXT
);
