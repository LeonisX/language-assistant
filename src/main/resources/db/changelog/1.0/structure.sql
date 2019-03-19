--liquibase formatted sql

--changeset leonis:1.0.2

CREATE TABLE Test.WordPlace (
    Word NVARCHAR(136) PRIMARY KEY,
    Place BIGINT NOT NULL
);

CREATE TABLE Test.WordFrequency (
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

-- trim sizes if need, add null restrictions
--changeset leonis:1.0.3

CREATE TABLE Test.ParsedRawData (
    itemId NVARCHAR(255) PRIMARY KEY,
    expression NVARCHAR(136) NOT NULL,
    audioFiles TEXT,
    thesaurus TEXT,
    definition TEXT,
    example TEXT,
    audience TEXT,
    cefr TEXT,
    gse TEXT,
    temporaryGse BOOLEAN,
    grammaticalCategories TEXT,
    collos TEXT,
    variants TEXT,
    topics TEXT,
    region TEXT,
    highlight TEXT,
);

--changeset leonis:1.0.4

CREATE INDEX words ON Test.WordLevel (Word);