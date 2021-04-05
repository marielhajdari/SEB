DROP TABLE IF EXISTS tournament CASCADE;
DROP TABLE IF EXISTS tourParticipants CASCADE;
DROP TABLE IF EXISTS history CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE users (
    username text PRIMARY KEY,
    pwd text NOT NULL,
    elo integer NOT NULL DEFAULT 100,
    logged boolean NOT NULL DEFAULT false,
    participating boolean NOT NULL DEFAULT false
);

CREATE TABLE history(
    entryid integer NOT NULL PRIMARY KEY,
    countPushUps integer, 
    durationInSeconds integer,
    usr_name text REFERENCES users(username)
);

CREATE TABLE tournament(
    id integer NOT NULL PRIMARY KEY,
    isActive boolean NOT NULL DEFAULT true
);

CREATE TABLE tourParticipants(
    participantID integer PRIMARY KEY,
    totalPushUps integer,
    participantName text REFERENCES users(username),
    tourId integer REFERENCES tournament(id)
);