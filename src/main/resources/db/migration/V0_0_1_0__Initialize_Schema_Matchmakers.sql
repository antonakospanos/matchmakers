create table "user"
(
  id          bigserial not null
    constraint user_pkey
    primary key,
  email       varchar(255),
  external_id uuid,
  name        varchar(255),
  password    varchar(255),
  username    varchar(255)
);

create table movie
(
  id               bigserial not null
    constraint movie_pkey
    primary key,
  description      varchar(255),
  external_id      uuid,
  publication_date timestamp,
  title            varchar(255),
  user_id          bigint
    constraint fknvsn9b9a8fok8dh383pspnxlq
    references "user"
);

create table movie_fans_associations
(
  movie_id bigint not null
    constraint fk99p98eljixhxxt98gdfm4xh70
    references movie,
  user_id  bigint not null
    constraint fk4u4np9rqbtjttwi3bq8gq3sno
    references "user",
  constraint movie_fans_associations_pkey
  primary key (movie_id, user_id)
);

create table movie_haters_associations
(
  movie_id bigint not null
    constraint fkjknrm6qevchwmj3yn32r6u0il
    references movie,
  user_id  bigint not null
    constraint fkpfmf1tugyi6jqp0809omntkn2
    references "user",
  constraint movie_haters_associations_pkey
  primary key (movie_id, user_id)
);

