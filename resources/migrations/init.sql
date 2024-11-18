create table if not exists end_user (
    username         text not null primary key,
    password         text not null,
    first_name       text not null,
    last_name        text not null,
    email            text not null unique,
    roles            text[] not null,
    active           boolean default false,
    created_at       timestamp with time zone default now(),
    email_activation boolean default false,
    login_fails      integer default 0
);

create table if not exists confirmation_token (
    username text not null primary key,
    token    text not null,
    valid_to timestamp with time zone not null,
    foreign key (username) references end_user(username)
);

insert into end_user (username, password, first_name, last_name, email, roles, active, created_at, email_activation, login_fails)
values ('admin', md5('Pan=pan7'), 'Panagiotis', 'Pliatsikas', 'icsdm322017@icsd.aegean.gr', '{ADMIN, USER}', true, now(), false, 0);

insert into end_user (username, password, first_name, last_name, email, roles, active, created_at, email_activation, login_fails)
values ('user1', md5('User=1'), 'Jim', 'Adams', 'ppliatsik@gmail.com', '{USER}', false, now(), false, 0);

create index if not exists idx_end_user_username on end_user(username);
