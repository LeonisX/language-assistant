--liquibase formatted sql

--changeset leonis:1.0.2

CREATE TABLE Bank.UserWordBank (
    Word NVARCHAR PRIMARY KEY,
    WordLevel VARCHAR(3) NOT NULL,
    Smoked BIGINT NOT NULL DEFAULT 0,
    Read BIGINT NOT NULL DEFAULT 0,
    Written BIGINT NOT NULL DEFAULT 0,
    Status TINYINT NOT NULL DEFAULT 0,
    RepeatTime TIMESTAMP,
    Level TINYINT NOT NULL DEFAULT 0
);

--changeset leonis:1.0.3

--ALTER TABLE Bank.UserWordBank ADD COLUMN WordLevel VARCHAR(3) NOT NULL;