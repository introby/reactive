CREATE TABLE vw
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO vw (name, creation_date, count)
VALUES ('VW Golf', '2004-10-19 10:23:54', 1),
       ('VW Polo', '2012-02-10 17:34:00', 4),
       ('VW Jetta', '2021-07-24 14:00:00', 2),
       ('VW Boro', '2020-01-20 00:00:00', 1),
       ('VW Passat', '2019-12-31 09:14:15', 7);

CREATE TABLE audi
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO audi (name, creation_date, count)
VALUES ('Audi A3', '2021-08-19', 19),
       ('Audi A6', '2019-10-10', 6),
       ('Audi Q6', '2020-03-11', 28),
       ('Audi TT', '2012-01-01', 1),
       ('Audi R8', '2019-12-31', 14);

CREATE TABLE seat
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO seat (name, creation_date, count)
VALUES ('Seat Leon', '2018-06-17 14:14:14', 17),
       ('Seat Ateca', '2020-07-30 08:34:00', 7),
       ('Seat Arona', '2020-02-28 17:35:00', 21),
       ('Seat Ibiza', '2018-12-12 21:14:21', 15),
       ('Seat Tarraco', '2019-02-07 09:42:15', 35);

CREATE TABLE car
(
    id                  SERIAL,
    name                varchar(100),
    creation_date       timestamp,
    count               int,
    PRIMARY KEY (id)
);

INSERT INTO car (name, creation_date, count)
VALUES ('Seat Leon', '2018-06-17 14:14:14', 17),
       ('Seat Ateca', '2020-07-30 08:34:00', 7),
       ('Seat Arona', '2020-02-28 17:35:00', 21),
       ('Seat Ibiza', '2018-12-12 21:14:21', 15),
       ('Seat Tarraco', '2019-02-07 09:42:15', 35);