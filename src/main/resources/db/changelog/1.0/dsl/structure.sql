--liquibase formatted sql

--changeset leonis:dsl:1.0.2

CREATE TABLE Bank.Raws (
    Id BIGINT IDENTITY PRIMARY KEY,
    Raw TEXT NOT NULL
);


-- trim sizes if need, add null restrictions
--changeset leonis:dsl:1.0.3

CREATE TABLE Bank.ParsedRawData (
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
