create table if not exists "DB_MIGRATION_HISTORY"
(
  version_rank   integer                 not null,
  installed_rank integer                 not null,
  version        varchar(50)             not null
    constraint "DB_MIGRATION_HISTORY_pk"
    primary key,
  description    varchar(200)            not null,
  type           varchar(20)             not null,
  script         varchar(1000)           not null,
  checksum       integer,
  installed_by   varchar(100)            not null,
  installed_on   timestamp default now() not null,
  execution_time integer                 not null,
  success        boolean                 not null
);

alter table "DB_MIGRATION_HISTORY"
  owner to matchmakers;

create index if not exists "DB_MIGRATION_HISTORY_vr_idx"
  on "DB_MIGRATION_HISTORY" (version_rank);

create index if not exists "DB_MIGRATION_HISTORY_ir_idx"
  on "DB_MIGRATION_HISTORY" (installed_rank);

create index if not exists "DB_MIGRATION_HISTORY_s_idx"
  on "DB_MIGRATION_HISTORY" (success);

create table if not exists blob
(
  id         bigserial not null
    constraint blob_pkey
    primary key,
  created_at timestamp,
  data       bytea,
  name       varchar(255),
  type       varchar(255)
);

alter table blob
  owner to matchmakers;

create table if not exists candidate
(
  id                  bigserial not null
    constraint candidate_pkey
    primary key,
  created_at          timestamp,
  email               varchar(255),
  external_id         uuid,
  name                varchar(255),
  password            varchar(255),
  username            varchar(255),
  cellphone           varchar(255),
  facebook_url        varchar(255),
  linked_in_url       varchar(255),
  registration_status varchar(255),
  image_id            bigint
    constraint fk5qtgp4rrmdtjtgg4eou1ag80d
    references blob,
  cv_id               bigint
    constraint fkjrue71rm1pljf4j81moo2062r
    references blob
);

alter table candidate
  owner to matchmakers;

create table if not exists candidate_education
(
  id           bigserial not null
    constraint candidate_education_pkey
    primary key,
  created_at   timestamp,
  degree       varchar(255),
  university   varchar(255),
  year_end     bigint,
  year_start   bigint,
  candidate_id bigint
    constraint fkhw87pfd8o09yvwivyfe3r5vep
    references candidate
);

alter table candidate_education
  owner to matchmakers;

create table if not exists candidate_experience
(
  id                          bigserial not null
    constraint candidate_experience_pkey
    primary key,
  created_at                  timestamp,
  degree_category             varchar(255),
  degree_level                varchar(255),
  extra_curriculum_activities boolean,
  senior_management           boolean,
  years                       bigint,
  candidate_id                bigint
    constraint fk7u9f84fswa8i2o9wjr1wa83u
    references candidate
);

alter table candidate_experience
  owner to matchmakers;

create table if not exists candidate_experience_work
(
  id                      bigserial not null
    constraint candidate_experience_work_pkey
    primary key,
  created_at              timestamp,
  company                 varchar(255),
  role                    varchar(255),
  year_end                bigint,
  year_start              bigint,
  candidate_experience_id bigint
    constraint fkok9980pwmlxknkx6ktrutfopv
    references candidate_experience
);

alter table candidate_experience_work
  owner to matchmakers;

create table if not exists candidate_objective
(
  id                     bigserial not null
    constraint candidate_objective_pkey
    primary key,
  created_at             timestamp,
  availability_interview date,
  availability_work      date,
  salary_from            bigint,
  salary_to              bigint,
  status                 varchar(255),
  candidate_id           bigint
    constraint fkte4lmf8rl8hebeeuds6i4qo4x
    references candidate
);

alter table candidate_objective
  owner to matchmakers;

create table if not exists candidate_experience_customers
(
  candidate_experience_id bigint not null
    constraint fk5ai2xijdj47vvglsihkhnrjiy
    references candidate_experience,
  customers               varchar(255)
);

alter table candidate_experience_customers
  owner to matchmakers;

create table if not exists candidate_experience_industries
(
  candidate_experience_id bigint not null
    constraint fkkfccrs3j98snbm5sjqc9xrbts
    references candidate_experience,
  industries              varchar(255)
);

alter table candidate_experience_industries
  owner to matchmakers;

create table if not exists candidate_experience_languages
(
  candidate_experience_id bigint not null
    constraint fko5omn2xqpgac5mwf3qvs82fqs
    references candidate_experience,
  languages               varchar(255)
);

alter table candidate_experience_languages
  owner to matchmakers;

create table if not exists candidate_experience_skills
(
  candidate_experience_id bigint not null
    constraint fk5n4t3yr41g52bdl4xgx4yapud
    references candidate_experience,
  skills                  varchar(255)
);

alter table candidate_experience_skills
  owner to matchmakers;

create table if not exists candidate_experience_specializations
(
  candidate_experience_id bigint not null
    constraint fkq4lshtdg392agvsnjp4jxdyhb
    references candidate_experience,
  specializations         varchar(255)
);

alter table candidate_experience_specializations
  owner to matchmakers;

create table if not exists candidate_objective_locations_primary
(
  candidate_objective_id bigint not null
    constraint fk1fjxufqmnqcc6b3j9tw8pssgk
    references candidate_objective,
  locations_primary      varchar(255)
);

alter table candidate_objective_locations_primary
  owner to matchmakers;

create table if not exists candidate_objective_locations_secondary
(
  candidate_objective_id bigint not null
    constraint fkitwabsa2wlm8tyo22vvu4wi3i
    references candidate_objective,
  locations_secondary    varchar(255)
);

alter table candidate_objective_locations_secondary
  owner to matchmakers;

create table if not exists candidate_objective_roles
(
  candidate_objective_id bigint not null
    constraint fk9c7ko64qa6x8ilh3i51d0mdvj
    references candidate_objective,
  roles                  varchar(255)
);

alter table candidate_objective_roles
  owner to matchmakers;
