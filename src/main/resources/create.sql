-- DROP DATABASE IF EXISTS bank_system;

/*
CREATE DATABASE javid_bank_system
    WITH
    ENCODING = 'UTF8';
*/

-- CREATE SCHEMA IF NOT EXISTS public;

-- SET SEARCH_PATH TO javid_bank_system, public;

-- DROP TYPE IF EXISTS TRANSACTION_STATUS;

CREATE TYPE TRANSACTION_STATUS AS ENUM (
    'CANCELED',
    'PENDING',
    'ACCEPTED'
    );

-- DROP TYPE IF EXISTS TRANSACTION_TYPE;

CREATE TYPE TRANSACTION_TYPE AS ENUM (
    'WITHDREW',
    'DEPOSIT'
    );

-- DROP TYPE IF EXISTS EMPLOYEE_ROLE;

CREATE TYPE EMPLOYEE_ROLE AS ENUM (
    'ADMIN',
    'BANK_MANAGER',
    'BRANCH_MANAGER',
    'CASHIER'
    );

-- DROP TABLE IF EXISTS customer;

CREATE TABLE IF NOT EXISTS customer
(
    id            BIGSERIAL PRIMARY KEY,
    firstname     VARCHAR(100),
    lastname      VARCHAR(100),
    national_code BIGINT UNIQUE
);

-- DROP TABLE IF EXISTS branch;

CREATE TABLE IF NOT EXISTS branch
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100),
    manager_id BIGINT UNIQUE
);

-- DROP TABLE IF EXISTS employee;

CREATE TABLE IF NOT EXISTS employee
(
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(100) UNIQUE,
    password   VARCHAR(100),
    branch_id  BIGINT,
    manager_id BIGINT,
    role       employee_role,
    CONSTRAINT fk_employee_branch FOREIGN KEY (branch_id) REFERENCES branch (id),
    CONSTRAINT fk_employee_manager FOREIGN KEY (manager_id) REFERENCES employee (id)
);

/*
ALTER TABLE branch
    DROP CONSTRAINT IF EXISTS fk_branch_manager;
*/
ALTER TABLE branch
    ADD CONSTRAINT fk_branch_manager FOREIGN KEY (manager_id) REFERENCES employee (id);

-- DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS account
(
    id          BIGSERIAL PRIMARY KEY,
    enabled     BOOLEAN,
    balance     BIGINT,
    customer_id BIGINT,
    branch_id   BIGINT,
    card_id     BIGINT,
    CONSTRAINT fk_account_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT fk_account_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

-- DROP TABLE IF EXISTS card;

CREATE TABLE IF NOT EXISTS card
(
    id          BIGSERIAL PRIMARY KEY,
    cvv2        INTEGER,
    expire_date DATE,
    enabled     BOOLEAN,
    card_number BIGINT,
    password1   INTEGER,
    password2   INTEGER,
    account_id  INTEGER,
    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES account (id) ON DELETE CASCADE
);

/*
ALTER TABLE account
    DROP CONSTRAINT IF EXISTS fk_account_card;
*/
ALTER TABLE account
    ADD CONSTRAINT fk_account_card FOREIGN KEY (card_id) REFERENCES card (id) ON DELETE SET NULL;

-- DROP TABLE IF EXISTS transactions;

CREATE TABLE IF NOT EXISTS transactions
(
    id         BIGSERIAL PRIMARY KEY,
    amount     BIGINT,
    account_id BIGINT,
    t_time     TIME,
    t_date     DATE,
    t_type     TRANSACTION_TYPE,
    t_status   TRANSACTION_STATUS,
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES account (id)
);