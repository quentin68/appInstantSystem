CREATE SCHEMA IF NOT EXISTS instant;
SET SCHEMA 'instant';

CREATE SEQUENCE IF NOT EXISTS parkings_seq;

CREATE TABLE IF NOT EXISTS instant.Parking (
  id INT DEFAULT NEXTVAL ('parkings_seq') PRIMARY KEY,
  name VARCHAR(30) NOT NULL,
  nbPlaceTotal INTEGER NOT NULL,
  longitude REAL NOT NULL,
  latitude REAL NOT NULL,
  cityname VARCHAR(30)NOT NULL
) ;

TRUNCATE TABLE instant.Parking;
