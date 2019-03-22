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

--changeset leonis:1.0.5

CREATE TABLE Test.UserWordBank (
    Word NVARCHAR(136) PRIMARY KEY,
    Smoke BOOLEAN DEFAULT FALSE,
    Reading BOOLEAN DEFAULT FALSE,
    Writing BOOLEAN DEFAULT FALSE,
    Smoked BIGINT NOT NULL DEFAULT 0,
    Read BIGINT NOT NULL DEFAULT 0,
    Written BIGINT NOT NULL DEFAULT 0
);

--changeset leonis:1.0.6
-- trim sizes if need, add null restrictions

CREATE TABLE Test.Dictionary (
    Id BIGINT IDENTITY PRIMARY KEY,
    LangFrom NVARCHAR(255),
    LangTo NVARCHAR(255),
    Format NVARCHAR(255),
    Revision NVARCHAR(255),
    FullName NVARCHAR(255),
    Size BIGINT NOT NULL DEFAULT 0,
    RecordsCount INT NOT NULL DEFAULT 0,
    Path NVARCHAR(255),
);

--changeset leonis:1.0.7

CREATE TABLE Test.WordToLearn (
    Word NVARCHAR(136) PRIMARY KEY,
    Smoke BOOLEAN DEFAULT FALSE,
    Reading BOOLEAN DEFAULT FALSE,
    Writing BOOLEAN DEFAULT FALSE,
    Smoked BIGINT NOT NULL DEFAULT 0,
    Read BIGINT NOT NULL DEFAULT 0,
    Written BIGINT NOT NULL DEFAULT 0
);