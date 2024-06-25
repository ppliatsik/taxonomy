create table if not exists end_user (
    username         text not null primary key,
    password         text not null,
    first_name       text not null,
    last_name        text not null,
    email            text not null unique,
    roles            text[] not null,
    active           boolean default false,
    created_at       timestamp with time zone default now(),
    email_activation boolean default false
);

create table if not exists confirmation_token (
    username text not null primary key,
    token    text not null,
    valid_to timestamp with time zone not null
);

insert into end_user (username, password, first_name, last_name, email, roles, active, created_at)
values ('panos', md5('Pan_pan7'), 'Panagiotis', 'Pliatsikas', 'icsdm322017@icsd.aegean.gr', '{admin}', true, now());

create table if not exists product (
    id           bigserial not null primary key,
    is_published boolean default false,
    created_by   text not null,
    created_at   timestamp with time zone default now()
);
