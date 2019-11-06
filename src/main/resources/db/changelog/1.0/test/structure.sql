--liquibase formatted sql

--changeset leonis:test:1.0.2

CREATE TABLE Bank.WordPlace (
    Word NVARCHAR(136) PRIMARY KEY,
    Place BIGINT NOT NULL
);

CREATE TABLE Bank.WordFrequency (
    Word NVARCHAR(136) PRIMARY KEY,
    Frequency BIGINT NOT NULL
);

CREATE TABLE Bank.WordLevel (
    Id BIGINT IDENTITY PRIMARY KEY,
    Word NVARCHAR(136) NOT NULL,
    Level VARCHAR(3) NOT NULL,
    Percent INT,
    Definition TEXT
);

CREATE INDEX words ON Bank.WordLevel (Word);

--changeset leonis:test:1.0.4

CREATE TABLE Bank.UserWordBank (
    Word NVARCHAR(136) PRIMARY KEY,
    Smoke BOOLEAN DEFAULT FALSE,
    Reading BOOLEAN DEFAULT FALSE,
    Writing BOOLEAN DEFAULT FALSE,
    Smoked BIGINT NOT NULL DEFAULT 0,
    Read BIGINT NOT NULL DEFAULT 0,
    Written BIGINT NOT NULL DEFAULT 0
);

--changeset leonis:test:1.0.5
-- trim sizes if need, add null restrictions

CREATE TABLE Bank.Dictionary (
    Id BIGINT IDENTITY PRIMARY KEY,
    LangFrom NVARCHAR(255),
    LangTo NVARCHAR(255),
    Format NVARCHAR(255),
    Revision NVARCHAR(255),
    FullName NVARCHAR(255),
    Size BIGINT NOT NULL DEFAULT 0,
    RecordsCount INT NOT NULL DEFAULT 0,
    Path NVARCHAR(255)
);

--changeset leonis:test:1.0.6

CREATE TABLE Bank.Variance (
    Id BIGINT IDENTITY PRIMARY KEY,
    Variance NVARCHAR(136) NOT NULL,
    Word NVARCHAR(136) NOT NULL
);
