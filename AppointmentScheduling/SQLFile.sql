-- -- Create customers table
-- CREATE TABLE customers (
--     customer_id SERIAL PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     email VARCHAR(100) UNIQUE NOT NULL
-- );

-- -- Create appointments table
-- CREATE TABLE appointments (
--     appointment_id SERIAL PRIMARY KEY,
--     customer_id INTEGER NOT NULL,
--     appointment_date TIMESTAMP NOT NULL,
--     reminder_message VARCHAR(255),
--     reminder_delay INTEGER,
--     FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
-- );

SELECT * FROM appointments;
SELECT * FROM customers;
