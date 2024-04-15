DROP SCHEMA IF EXISTS "order" CASCADE;

CREATE SCHEMA "order";

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING', 'IN_DELIVERY', 'DELIVERED');

DROP TABLE IF EXISTS "order".orders CASCADE;

CREATE TABLE "order".orders
(
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    order_status order_status NOT NULL,
    failure_messages character varying COLLATE pg_catalog."default",
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS "order".order_items CASCADE;

CREATE TABLE "order".order_items
(
    id bigint NOT NULL,
    order_id uuid NOT NULL,
    product_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    quantity integer NOT NULL,
    sub_total numeric(10,2) NOT NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (id, order_id)
);

ALTER TABLE "order".order_items
    ADD CONSTRAINT "FK_ORDER_ID" FOREIGN KEY (order_id)
    REFERENCES "order".orders (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;

DROP TABLE IF EXISTS "order".order_address CASCADE;

CREATE TABLE "order".order_address
(
    id uuid NOT NULL,
    order_id uuid UNIQUE NOT NULL,
    street character varying COLLATE pg_catalog."default" NOT NULL,
    postal_code character varying COLLATE pg_catalog."default" NOT NULL,
    city character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT order_address_pkey PRIMARY KEY (id, order_id)
);

ALTER TABLE "order".order_address
    ADD CONSTRAINT "FK_ORDER_ID" FOREIGN KEY (order_id)
    REFERENCES "order".orders (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;

DROP TABLE IF EXISTS "order".customers CASCADE;

CREATE TABLE "order".customers
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);


DROP SCHEMA IF EXISTS customer CASCADE;

CREATE SCHEMA customer;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE customer.customers
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);



DROP VIEW IF EXISTS customer.order_customer_view;


CREATE VIEW customer.order_customer_view AS
SELECT id,
    username,
    first_name,
    last_name
   FROM customer.customers;


INSERT INTO customer.customers
(id, username, first_name, last_name)
VALUES('d215b5f8-0249-4dc5-89a3-51fd148cfb41'::uuid, 'oussama.abouzid', 'oussama', 'abouzid');


DROP SCHEMA IF EXISTS payment CASCADE;

CREATE SCHEMA payment;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE payment.payments
(
    id uuid NOT NULL,
    amount numeric(10,2) NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    CONSTRAINT payments_pkey PRIMARY KEY (id)
);

CREATE TABLE payment.payments_threshold
(
    id uuid NOT NULL,
    total_amount numeric(10,2) NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    CONSTRAINT payments_threshold_pkey PRIMARY KEY (id)
);