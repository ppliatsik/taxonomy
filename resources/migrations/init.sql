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
    delivery_method                            text,
    layout_model                               text,
    category_of_services_belongs               text,
    cost_model                                 text,
    implemented_security_mechanisms_basic      text,
    implemented_security_mechanisms_additional text,
    non_functional_guarantees                  text,
    type_protection_offered                    text,
    security_features                          text,
    protected_items                            text,
    threats_addressed                          text,
    restrictions                               text,
    free_versions_existence                    text,
    how_to_use_product                         text,
    company_offering_product                   text,
    marketplace_product_offered                text,
    level_type_product_support                 text,
    foreign key (created_by) references end_user(username)
);
