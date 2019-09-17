CREATE SEQUENCE companies_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

create table companies (
  id bigint not null DEFAULT nextval('companies_id_seq'::regclass),
  created timestamp without time zone,
  updated timestamp without time zone,
  name text,
  deleted boolean DEFAULT false,
  website text,
  CONSTRAINT companies_pkey PRIMARY KEY (id)
);

