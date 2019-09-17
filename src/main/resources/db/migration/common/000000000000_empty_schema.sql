CREATE USER dbmodels WITH PASSWORD 'db-models!2019';

create database db_test;

GRANT ALL PRIVILEGES ON DATABASE db_test TO dbmodels;
