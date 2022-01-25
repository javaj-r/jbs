SELECT (date_trunc('MONTH', current_date::date) + INTERVAL '3 YEAR + 1 MONTH - 1 day')::DATE;
SELECT (date_trunc('month', current_date::date) + interval '3 year' + interval '1 month' - interval '1 day')::date
           AS end_of_month;
select current_date;

SELECT (date_trunc('MONTH', '2022-1-1'::date) + INTERVAL '1 MONTH - 1 day')::DATE;
SELECT (current_date)::DATE;


-- SELECT b.id
--      , b.name
--      , b.address_id
--      , a.country
--      , a.state
--      , a.city
--      , a.street
--      , a.postal_code
--      , b.manager_id
--      , b.bank_id
--      , b2.name       bank_name
--      , b2.manager_id bank_manager_id
-- FROM branch b
--          LEFT JOIN address a on a.id = b.address_id
--          LEFT JOIN employee e on b.id = e.branch_id
--          LEFT JOIN bank b2 on b2.id = b.bank_id
-- WHERE b.id = ?;

INSERT INTO branch(name)
values (?, ?);

UPDATE branch
SET name=?,
    manager_id=?
WHERE id=?;

SELECT id
     , balance
     , enabled
     , branch_id
     , card_id
     , customer_id
FROM account;

INSERT INTO account(enabled, balance, customer_id, branch_id, card_id)
values (?, ?, ?, ?, ?);

UPDATE account
SET enabled=?,
    balance=?,
    card_id=?
WHERE id=?;

SELECT id
     , cvv2
     , expire_date
     , enabled
     , card_number
     , password1
     , password2
     , account_id
FROM card;

INSERT INTO card(cvv2, expire_date, enabled, card_number, password1, password2, account_id)
values (?, ?, ?, ?, ?, ?, ?);

UPDATE card
SET cvv2=?,
    expire_date=?,
    enabled =?,
    card_number=?,
    password1 =?,
    password2 =?,
    account_id =?
WHERE id=?;

-- SELECT c.id, p.firstname, p.lastname, p.national_code
-- FROM customer c
--          LEFT JOIN person p on p.id = c.person_id;

-- WITH data(person_id) AS (
--     INSERT INTO person (firstname, lastname, national_code)
--         VALUES (?, ?, ?)
--         RETURNING id
-- )
-- INSERT
-- INTO customer (person_id)
-- SELECT d.person_id
-- FROM data d;


DELETE
FROM employee
WHERE id = ?;

UPDATE employee
SET branch_id=?,
    manager_id=?
WHERE id=?;



SELECT id, amount, t_time, t_date, t_type, t_status, account_id
FROM transactions;

INSERT INTO transactions(amount, account_id, t_time, t_date, t_type, t_status)
VALUES (?, ?, current_time, current_date, ?, ?);

UPDATE transactions
SET amount=?,
    account_id=?,
    t_type=?,
    t_status=?
WHERE id=?;

-- SELECT b.id
--      , b.name
--      , b.address_id
--      , b.manager_id
--      , b.bank_id
-- FROM branch b
--          LEFT JOIN address a on a.id = b.address_id
--          LEFT JOIN employee e on b.id = e.branch_id
--          LEFT JOIN bank b2 on b2.id = b.bank_id;

SELECT id
     , name
     , manager_id
FROM branch;


SELECT *
FROM employee
where 1 = 1
order by id;

INSERT INTO customer (firstname, lastname, national_code)
VALUES (?, ?, ?)