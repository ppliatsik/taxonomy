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
    valid_to timestamp with time zone not null,
    foreign key (username) references end_user(username)
);

insert into end_user (username, password, first_name, last_name, email, roles, active, created_at, email_activation)
values ('panos', md5('Pan_pan7'), 'Panagiotis', 'Pliatsikas', 'icsdm322017@icsd.aegean.gr', '{admin, user}', true, now(), false);

create table if not exists product (
    id                                         bigserial not null primary key,
    is_published                               boolean default false,
    created_by                                 text not null,
    created_at                                 timestamp with time zone default now(),
    name                                       text not null unique,
    delivery_methods                           text[],
    layout_models                              text[],
    product_categories                         text[],
    cost_model_types                           text[],
    cost_model_packets                         int,
    cost_model_charge                          jsonb,
    minimum_cost                               decimal(15, 2),
    implemented_security_mechanisms            text,--
    non_functional_guarantees                  text,--
    protection_types                           text[],
    security_features                          text[],
    protected_items                            text[],
    threats_faced                              text,--
    restrictions                               text,--
    version_type                               text,
    free_version_details                       jsonb,
    product_usage                              text[],
    product_company                            text,
    marketplaces                               text[],
    product_support                            text,--
    foreign key (created_by) references end_user(username)
);

create index if not exists idx_end_user_username on end_user(username);
create index if not exists idx_product_created_by on product(created_by);
