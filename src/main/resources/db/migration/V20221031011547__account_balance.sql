Create MATERIALIZED VIEW account_balance as
select aa.start_balance + (income.value - outcome.value) as balance, aa.id, aa.name from accounts aa, (
    select a.id, sum(t.value)as income
    from accounts a join transactions t on a.id = t.account_id
    where t.direction = 'IN' group by a.id) as income(id, value) join
(
    select a.id, sum(t.value) as outcome
    from accounts a join transactions t on a.id = t.account_id
    where t.direction = 'OUT' group by a.id) as outcome(id, value)
on
        outcome.id = income.id;