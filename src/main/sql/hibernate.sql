create table hibernate (
    id serial
        constraint table_name_pk
        primary key,
    title varchar(20),
    text varchar
);