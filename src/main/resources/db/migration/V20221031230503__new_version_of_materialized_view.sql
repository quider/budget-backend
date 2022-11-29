
drop materialized view account_balance;

Create MATERIALIZED VIEW account_balance as
select income.value as balance, aa.id, aa.name, aa.type from accounts aa join (
    select a.id, sum(t.value)as income
    from accounts a  join transactions t on a.id = t.account_id
    group by a.id ) as income(id, value)
                                                                     on aa.id = income.id;