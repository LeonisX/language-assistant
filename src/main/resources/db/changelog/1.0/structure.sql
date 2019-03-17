--liquibase formatted sql

--changeset leonis:1.0.0.2

--todo test, delete
CREATE TABLE Test.Persons (
    Id BIGINT IDENTITY PRIMARY KEY,
    Full_Name NVARCHAR(64) NOT NULL,
    Date_Of_Birth DATETIME2 NOT NULL
);
