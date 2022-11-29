alter table transactions
    add account_id uuid not null;

alter table transactions
    add constraint transactions_accounts_id_fk
        foreign key (account_id) references accounts;
