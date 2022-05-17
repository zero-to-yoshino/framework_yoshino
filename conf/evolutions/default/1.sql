# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table entry (
  id                            bigint,
  name                          varchar(255),
  title                         varchar(255),
  message                       varchar(255),
  create_date                   timestamp,
  update_date                   timestamp not null
);


# --- !Downs

drop table if exists entry;

