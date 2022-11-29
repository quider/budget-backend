
create table categories
(
    id bigserial,
    name varchar(20),
    created timestamp not null,
    modified timestamp not null
);

create unique index category_id_uindex
    on categories (id);

create unique index category_name_uindex
    on categories (name);

alter table categories
    add constraint category_pk
        primary key (id);

create table accounts
(
    id uuid,
    name varchar(20) not null,
    icon varchar(200) not null,
    start_balance decimal(18,2) not null,
    created timestamp not null,
    modified timestamp not null,
    closed timestamp
);

create unique index account_id_uindex
    on accounts (id);

create unique index account_name_uindex
    on accounts (name);

alter table accounts
    add constraint account_pk
        primary key (id);


create table transactions
(
    id uuid not null,
    description varchar(250) not null,
    direction varchar(6) not null,
    value decimal(18,2) not null,
    balance_after decimal(18,2) not null,
    transaction_date timestamp not null,
    category_id bigint not null
        constraint table_name_category_id_fk
            references categories (id)
);

create unique index transactions_id_uindex
    on transactions (id);

alter table transactions
    add constraint transactions_pk
        primary key (id);

