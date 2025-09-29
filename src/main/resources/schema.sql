DROP TABLE IF EXISTS earthquake;
CREATE TABLE earthquake
(
    id varchar(36) not null primary key,
    deep int4 not null,
    type varchar(255),
    magnitude float8,
    state varchar(255),
    date_time timestamp
);