-- Employee related tables start --
CREATE TABLE IF NOT EXISTS employee
(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    mobile VARCHAR NOT NULL,
    joining_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS manager
(
    id SERIAL PRIMARY KEY,
    branch VARCHAR NOT NULL,
    employee_id INT NOT NULL REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS hr
(
    id SERIAL PRIMARY KEY,
    is_trainer BOOLEAN NOT NULL,
    employee_id INT NOT NULL REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS salesperson
(
    id SERIAL PRIMARY KEY,
    number_of_sales INT NOT NULL,
    employee_id INT NOT NULL REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS accountant
(
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS darryl
(
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employee(id)
);
-- Employee related tables end --

-- Supporting tables --
CREATE TABLE IF NOT EXISTS customer
(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    mobile VARCHAR NOT NULL,
    registration_date TIMESTAMP NOT NULL,
    employee_id INT NOT NULL REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS item
(
    id SERIAL PRIMARY KEY,
    quantity_available INT NOT NULL,
    price INT NOT NULL
);
-- Supporting tables end --

-- Adds a partitioned table "sale" partitioned by "date" in 4 quarters, only works for 2022 currently.
CREATE TABLE IF NOT EXISTS sale
(
    id SERIAL NOT NULL,
    quantity INT NOT NULL,
    date DATE NOT NULL,
    employee_id INT NOT NULL REFERENCES employee(id),
    customer_id INT NOT NULL REFERENCES customer(id),
    PRIMARY KEY(id, date)
) PARTITION BY RANGE(date);

CREATE TABLE IF NOT EXISTS sale_q1 PARTITION OF sale FOR VALUES FROM ('2022-01-01') to ('2022-04-01');
CREATE TABLE IF NOT EXISTS sale_q2 PARTITION OF sale FOR VALUES FROM ('2022-04-01') to ('2022-07-01');
CREATE TABLE IF NOT EXISTS sale_q3 PARTITION OF sale FOR VALUES FROM ('2022-07-01') to ('2022-10-01');
CREATE TABLE IF NOT EXISTS sale_q4 PARTITION OF sale FOR VALUES FROM ('2022-10-01') to ('2023-01-01');

-- Creates index on "date" column for "sale" and its partitions and clusters by date
CREATE INDEX IF NOT EXISTS sale_date_idx ON sale(date);
CLUSTER sale_q1 USING sale_q1_date_idx;
CLUSTER sale_q2 USING sale_q2_date_idx;
CLUSTER sale_q3 USING sale_q3_date_idx;
CLUSTER sale_q4 USING sale_q4_date_idx;

-- M-N relational table for mapping sales and items
CREATE TABLE IF NOT EXISTS sale_item
(
    sale_id INT NOT NULL,
    sale_date DATE NOT NULL,
    item_id INT NOT NULL REFERENCES item(id),
    FOREIGN KEY (sale_id, sale_date) REFERENCES sale(id, date)
);
