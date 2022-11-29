create table transfers
(
    id bigserial,
    source_id uuid not null
        constraint transfers_transactions_id_fk
            references transactions,
    target_id uuid not null
        constraint transfers_transactions_id_fk_2
            references transactions,
    timestamp timestamp not null
);

create unique index transfers_id_uindex
    on transfers (id);

alter table transfers
    add constraint transfers_pk
        primary key (id);
