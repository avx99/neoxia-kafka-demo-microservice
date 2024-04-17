DROP SCHEMA IF EXISTS nxm CASCADE;

CREATE SCHEMA nxm;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TYPE IF EXISTS order_status;
CREATE TYPE order_status AS ENUM ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING', 'IN_DELIVERY', 'DELIVERED');

DROP TABLE IF EXISTS nxm.orders CASCADE;

CREATE TABLE nxm.orders
(
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    price numeric(10,2) NOT NULL,
    order_status order_status NOT NULL,
    failure_messages character varying COLLATE pg_catalog."default",
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);


DROP TABLE IF EXISTS nxm.customers CASCADE;


CREATE TABLE nxm.customers
(
    id uuid NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customers_pkey PRIMARY KEY (id)
);




INSERT INTO nxm.customers
(id, username, first_name, last_name)
VALUES('d215b5f8-0249-4dc5-89a3-51fd148cfb41'::uuid, 'oussama.abouzid', 'oussama', 'abouzid');

DROP TABLE IF EXISTS nxm.payments CASCADE;

CREATE TABLE nxm.payments
(
    id uuid NOT NULL,
    amount numeric(10,2) NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    CONSTRAINT payments_pkey PRIMARY KEY (id)
);

DROP TABLE IF EXISTS nxm.payments_threshold CASCADE;

CREATE TABLE nxm.payments_threshold
(
    id uuid NOT NULL,
    total_amount numeric(10,2) NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    CONSTRAINT payments_threshold_pkey PRIMARY KEY (id)
);

insert into nxm.payments_threshold (id, total_amount, customer_id, order_id) values ('2c3faa84-7047-4e41-a80b-95e03db6e2bb'::uuid, 324322, 'd215b5f8-0249-4dc5-89a3-51fd148cfb41'::uuid, 'bd6c5d4d-f8c3-40b1-be42-70352dd42b84'::uuid);


DROP TABLE IF EXISTS nxm.delivery CASCADE;

CREATE TABLE nxm.delivery
(
    id uuid NOT NULL,
    customer_id uuid NOT NULL,
    order_id uuid NOT NULL,
    address character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT delivery_pkey PRIMARY KEY (id)
);

