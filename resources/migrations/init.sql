create table if not exists end_user (
    username   text not null primary key,
    password   text not null,
    first_name text not null,
    last_name  text not null,
    email      text not null,
    roles      text[] not null,
    active     boolean default false,
    created_at timestamp with time zone default now()
);

insert into end_user (username, password, first_name, last_name, email, roles, active, created_at)
values ('panos', md5('Pan_pan7'), 'Panagiotis', 'Pliatsikas', 'icsdm322017@icsd.aegean.gr', '{admin}', true, now());

create table if not exists product ();
