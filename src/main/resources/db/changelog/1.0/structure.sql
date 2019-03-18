--liquibase formatted sql

--changeset leonis:1.0.2

--todo test, delete
CREATE TABLE Test.Persons (
    Id BIGINT IDENTITY PRIMARY KEY,
    Full_Name NVARCHAR(64) NOT NULL,
    Date_Of_Birth DATETIME2 NOT NULL
);

--changeset leonis:1.0.3

CREATE TABLE Bank.WordPlaces (
    Word NVARCHAR(136) PRIMARY KEY,
    Place BIGINT NOT NULL
);

CREATE TABLE Bank.WordFrequencies (
    Word NVARCHAR(136) PRIMARY KEY,
    Frequency BIGINT NOT NULL
);

CREATE TABLE Bank.WordLevels (
    Word NVARCHAR(136) PRIMARY KEY,
    Level VARCHAR(3) NOT NULL,
    Percent INT
);

--changeset leonis:1.0.4

CREATE TABLE Test.Raws (
    Id BIGINT IDENTITY PRIMARY KEY,
    Raw TEXT NOT NULL
);