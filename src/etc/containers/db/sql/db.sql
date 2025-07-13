-- PostgreSQL Database Setup Script
-- Execute these commands in order

-- 1. Create user with password
CREATE USER sas_app WITH ENCRYPTED PASSWORD 'abcd4321';

-- 2. Create databases with the user as owner
CREATE DATABASE sas_db OWNER sas_app;
CREATE DATABASE sas_auth_server_db OWNER sas_app;

-- 3. Grant all privileges to the user on the databases
GRANT ALL PRIVILEGES ON DATABASE sas_db TO sas_app;
GRANT ALL PRIVILEGES ON DATABASE sas_auth_server_db TO sas_app;

GRANT ALL ON SCHEMA public TO sas_app;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO sas_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO sas_app;


-- 3. Connect to sas_db and create all objects there
\c sas_db

-- public.t_plan_config_seq definition

-- DROP SEQUENCE t_plan_config_seq;

CREATE SEQUENCE t_plan_config_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_plan_seq definition

-- DROP SEQUENCE t_plan_seq;

CREATE SEQUENCE t_plan_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_registration_token_seq definition

-- DROP SEQUENCE t_registration_token_seq;

CREATE SEQUENCE t_registration_token_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_subscriber_plan_seq definition

-- DROP SEQUENCE t_subscriber_plan_seq;

CREATE SEQUENCE t_subscriber_plan_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_subscriber_seq definition

-- DROP SEQUENCE t_subscriber_seq;

CREATE SEQUENCE t_subscriber_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_user_seq definition

-- DROP SEQUENCE t_user_seq;

CREATE SEQUENCE t_user_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;

-- public.t_billing_config_seq definition

-- DROP SEQUENCE t_billing_config_seq;

CREATE SEQUENCE t_billing_config_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_billing_installments_seq definition

-- DROP SEQUENCE t_billing_installments_seq;

CREATE SEQUENCE t_billing_installments_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_billing_seq definition

-- DROP SEQUENCE t_billing_seq;

CREATE SEQUENCE t_billing_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_customer_address_seq definition

-- DROP SEQUENCE t_customer_address_seq;

CREATE SEQUENCE t_customer_address_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_customer_contact_seq definition

-- DROP SEQUENCE t_customer_contact_seq;

CREATE SEQUENCE t_customer_contact_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_customer_seq definition

-- DROP SEQUENCE t_customer_seq;

CREATE SEQUENCE t_customer_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_order_seq definition

-- DROP SEQUENCE t_order_seq;

CREATE SEQUENCE t_order_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_production_order_seq definition

-- DROP SEQUENCE t_production_order_seq;

CREATE SEQUENCE t_production_order_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;


-- public.t_resource_seq definition

-- DROP SEQUENCE t_resource_seq;

CREATE SEQUENCE t_resource_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 50
	NO CYCLE;



-- public.t_plan definition

-- Drop table

-- DROP TABLE t_plan;

CREATE TABLE t_plan (
	id int2 NOT NULL,
	created_at timestamp(6) NOT NULL,
	description varchar(255) NOT NULL,
	"name" varchar(20) NOT NULL,
	plan_key varchar(50) NOT NULL,
	status int2 NOT NULL,
	updated_at timestamp(6) NOT NULL,
	CONSTRAINT t_plan_pkey PRIMARY KEY (id),
	CONSTRAINT t_plan_status_check CHECK (((status >= 0) AND (status <= 1))),
	CONSTRAINT uk7he4oix42qlmyli1uckokvbpx UNIQUE (plan_key)
);


-- public.t_subscriber definition

-- Drop table

-- DROP TABLE t_subscriber;

CREATE TABLE t_subscriber (
	id int8 NOT NULL,
	activated_at timestamp(6) NULL,
	address varchar(150) NOT NULL,
	country varchar(20) NOT NULL,
	created_at timestamp(6) NOT NULL,
	email varchar(50) NOT NULL,
	"name" varchar(60) NOT NULL,
	phone varchar(15) NOT NULL,
	status int2 NOT NULL,
	terms_and_conditions_accepted bool NOT NULL,
	updated_at timestamp(6) NOT NULL,
	"uuid" varchar(60) NOT NULL,
	CONSTRAINT t_subscriber_pkey PRIMARY KEY (id),
	CONSTRAINT t_subscriber_status_check CHECK (((status >= 0) AND (status <= 3)))
);


-- public.t_voucher definition

-- Drop table

-- DROP TABLE t_voucher;

CREATE TABLE t_voucher (
	code varchar(30) NOT NULL,
	created_at timestamp(6) NOT NULL,
	status int2 NOT NULL,
	updated_at timestamp(6) NOT NULL,
	CONSTRAINT t_voucher_pkey PRIMARY KEY (code),
	CONSTRAINT t_voucher_status_check CHECK (((status >= 0) AND (status <= 1)))
);


-- public.t_plan_config definition

-- Drop table

-- DROP TABLE t_plan_config;

CREATE TABLE t_plan_config (
	id int2 NOT NULL,
	created_at timestamp(6) NOT NULL,
	currency varchar(5) NOT NULL,
	max_users int4 NOT NULL,
	price float8 NOT NULL,
	updated_at timestamp(6) NOT NULL,
	plan_id int2 NULL,
	CONSTRAINT t_plan_config_pkey PRIMARY KEY (id),
	CONSTRAINT ukf85g10rc1yj2tog0l2npq9u1h UNIQUE (plan_id),
	CONSTRAINT fkfy7xtgk9h20wmy2gujw6041tl FOREIGN KEY (plan_id) REFERENCES t_plan(id)
);


-- public.t_subscriber_plan definition

-- Drop table

-- DROP TABLE t_subscriber_plan;

CREATE TABLE t_subscriber_plan (
	subscriber_plan_id int8 NOT NULL,
	created_at timestamp(6) NOT NULL,
	status int2 NOT NULL,
	updated_at timestamp(6) NOT NULL,
	subscriber_id int8 NOT NULL,
	plan_id int2 NOT NULL,
	voucher_id varchar(30) NULL,
	CONSTRAINT t_subscriber_plan_pkey PRIMARY KEY (subscriber_plan_id, plan_id, subscriber_id),
	CONSTRAINT t_subscriber_plan_status_check CHECK (((status >= 0) AND (status <= 1))),
	CONSTRAINT fk1mktnrxg3160kg9tjj9sbnvkj FOREIGN KEY (plan_id) REFERENCES t_plan(id),
	CONSTRAINT fk3o2xsjggvoexidd2kwa2xddn9 FOREIGN KEY (voucher_id) REFERENCES t_voucher(code),
	CONSTRAINT fklc1f62p325owu7wes4x951wq3 FOREIGN KEY (subscriber_id) REFERENCES t_subscriber(id)
);


-- public.t_user definition

-- Drop table

-- DROP TABLE t_user;

CREATE TABLE t_user (
	id int8 NOT NULL,
	created_at timestamp(6) NOT NULL,
	email varchar(50) NOT NULL,
	first_name varchar(50) NULL,
	hash_password varchar(100) NOT NULL,
	is_admin bool NOT NULL,
	last_name varchar(50) NULL,
	status int2 NOT NULL,
	updated_at timestamp(6) NOT NULL,
	user_name varchar(50) NOT NULL,
	"uuid" varchar(60) NOT NULL,
	subscriber_id int8 NULL,
	CONSTRAINT t_user_pkey PRIMARY KEY (id),
	CONSTRAINT t_user_status_check CHECK (((status >= 0) AND (status <= 3))),
	CONSTRAINT fkf0jttdugkiy7krif59rwra954 FOREIGN KEY (subscriber_id) REFERENCES t_subscriber(id)
);


-- public.t_registration_token definition

-- Drop table

-- DROP TABLE t_registration_token;

CREATE TABLE t_registration_token (
	id int8 NOT NULL,
	created_at timestamp(6) NOT NULL,
	expires_at timestamp(6) NOT NULL,
	"token" varchar(30) NOT NULL,
	"type" int2 NOT NULL,
	used bool NOT NULL,
	subscriber_id int8 NULL,
	user_id int8 NULL,
	CONSTRAINT t_registration_token_pkey PRIMARY KEY (id),
	CONSTRAINT t_registration_token_type_check CHECK (((type >= 0) AND (type <= 3))),
	CONSTRAINT fk6cb0buys9xsgnxqku5nphmqwf FOREIGN KEY (subscriber_id) REFERENCES t_subscriber(id),
	CONSTRAINT fkmwldovryv6gj6e7wsfk9mki8d FOREIGN KEY (user_id) REFERENCES t_user(id)
);

-- public.t_billing_config definition

-- Drop table

-- DROP TABLE t_billing_config;

CREATE TABLE t_billing_config (
	id_billing_config int4 NOT NULL,
	billing_config_installments_interval_unit varchar(255) NULL,
	billing_config_installments_interval int2 NULL,
	billing_config_installments_quantity int2 NULL,
	billing_config_name varchar(255) NULL,
	billing_config_payment_type varchar(255) NULL,
	CONSTRAINT t_billing_config_billing_config_installments_interval_uni_check CHECK (((billing_config_installments_interval_unit)::text = ANY ((ARRAY['MONTH'::character varying, 'WEEK'::character varying, 'DAY'::character varying])::text[]))),
	CONSTRAINT t_billing_config_billing_config_payment_type_check CHECK (((billing_config_payment_type)::text = ANY ((ARRAY['INSTALLMENTS'::character varying, 'ONE_TIME'::character varying])::text[]))),
	CONSTRAINT t_billing_config_pkey PRIMARY KEY (id_billing_config)
);


-- public.t_resource definition

-- Drop table

-- DROP TABLE t_resource;

CREATE TABLE t_resource (
	id_resource int4 NOT NULL,
	resource_name varchar(255) NOT NULL,
	CONSTRAINT t_resource_pkey PRIMARY KEY (id_resource)
);




-- public.t_customer definition

-- Drop table

-- DROP TABLE t_customer;

CREATE TABLE t_customer (
	id_customer int4 NOT NULL,
	cust_details varchar(10000) NULL,
	cust_name varchar(30) NOT NULL,
	cust_status int2 NOT NULL,
	id_subscriber int8 NOT NULL,
	CONSTRAINT t_customer_cust_status_check CHECK (((cust_status >= 0) AND (cust_status <= 1))),
	CONSTRAINT t_customer_pkey PRIMARY KEY (id_customer),
	CONSTRAINT fk3tcyqlnpsxvwonl2hy2p1bgv6 FOREIGN KEY (id_subscriber) REFERENCES t_subscriber(id)
);


-- public.t_customer_address definition

-- Drop table

-- DROP TABLE t_customer_address;

CREATE TABLE t_customer_address (
	id_address int4 NOT NULL,
	city varchar(45) NULL,
	complement varchar(45) NULL,
	country varchar(3) NULL,
	"number" int4 NULL,
	state varchar(3) NULL,
	street_name varchar(60) NULL,
	zipcode varchar(15) NULL,
	id_customer int4 NULL,
	CONSTRAINT t_customer_address_pkey PRIMARY KEY (id_address),
	CONSTRAINT fkl2owuxq2vshrv8g0p6hxsdbxj FOREIGN KEY (id_customer) REFERENCES t_customer(id_customer)
);


-- public.t_customer_contact definition

-- Drop table

-- DROP TABLE t_customer_contact;

CREATE TABLE t_customer_contact (
	id_contact int4 NOT NULL,
	email varchar(45) NULL,
	mobile_phone_number varchar(20) NULL,
	"name" varchar(45) NULL,
	office_phone_number varchar(20) NULL,
	contact_status int2 NOT NULL,
	id_customer int4 NULL,
	CONSTRAINT t_customer_contact_contact_status_check CHECK (((contact_status >= 0) AND (contact_status <= 1))),
	CONSTRAINT t_customer_contact_pkey PRIMARY KEY (id_contact),
	CONSTRAINT fkpk14w0wx2hfam2cc92bg7vht8 FOREIGN KEY (id_customer) REFERENCES t_customer(id_customer)
);


-- public.t_individual definition

-- Drop table

-- DROP TABLE t_individual;

CREATE TABLE t_individual (
	cpf varchar(14) NOT NULL,
	rg varchar(255) NULL,
	id_customer int4 NOT NULL,
	CONSTRAINT t_individual_pkey PRIMARY KEY (id_customer),
	CONSTRAINT fkrma0n3mr3197ur99o8cwiqr11 FOREIGN KEY (id_customer) REFERENCES t_customer(id_customer)
);


-- public.t_order definition

-- Drop table

-- DROP TABLE t_order;

CREATE TABLE t_order (
	id_order int8 NOT NULL,
	close_date timestamp(6) NULL,
	deliver_date timestamp(6) NULL,
	order_description varchar(255) NOT NULL,
	open_date timestamp(6) NOT NULL,
	order_code varchar(100) NOT NULL,
	planned_deliver_date timestamp(6) NULL,
	order_price float8 NULL,
	qtd_item float8 NOT NULL,
	order_status int2 NOT NULL,
	id_customer int4 NULL,
	id_subscriber int8 NULL,
	CONSTRAINT t_order_order_status_check CHECK (((order_status >= 0) AND (order_status <= 6))),
	CONSTRAINT t_order_pkey PRIMARY KEY (id_order),
	CONSTRAINT ukgevcb8e17hlrdyjyu2e44inja UNIQUE (order_code),
	CONSTRAINT fk1u2d3wjkr2lu2lsb9atinb6lc FOREIGN KEY (id_customer) REFERENCES t_customer(id_customer),
	CONSTRAINT fk3xptvqd9de25lmkob9nbji8ss FOREIGN KEY (id_subscriber) REFERENCES t_subscriber(id)
);


-- public.t_production_order definition

-- Drop table

-- DROP TABLE t_production_order;

CREATE TABLE t_production_order (
	id_po int8 NOT NULL,
	actual_completion_date date NULL,
	actual_start_date date NULL,
	estimated_completion_date date NULL,
	estimated_time float8 NOT NULL,
	instructions varchar(255) NULL,
	open_date date NOT NULL,
	planned_start_date date NOT NULL,
	po_number varchar(100) NOT NULL,
	priority int2 NOT NULL,
	qtd_pieces int4 NULL,
	po_status int2 NOT NULL,
	id_order int8 NULL,
	id_subscriber int8 NOT NULL,
	CONSTRAINT t_production_order_pkey PRIMARY KEY (id_po),
	CONSTRAINT t_production_order_po_status_check CHECK (((po_status >= 0) AND (po_status <= 3))),
	CONSTRAINT t_production_order_priority_check CHECK (((priority >= 0) AND (priority <= 3))),
	CONSTRAINT ukqv0k5jp3hqh2n0gv3xwf76ijm UNIQUE (po_number),
	CONSTRAINT fkche61vvnhu6m3h89rcm639nyp FOREIGN KEY (id_order) REFERENCES t_order(id_order),
	CONSTRAINT fkps10kihe2248xqh6ee7lwngcl FOREIGN KEY (id_subscriber) REFERENCES t_subscriber(id)
);




-- public.t_billing definition

-- Drop table

-- DROP TABLE t_billing;

CREATE TABLE t_billing (
	id_billing int8 NOT NULL,
	billing_calculated_value float8 NOT NULL,
	billing_closed_at date NULL,
	billing_created_at date NOT NULL,
	billing_start_date date NOT NULL,
	billing_final_value float8 NOT NULL,
	billing_status int2 NOT NULL,
	billing_uuid varchar(255) NOT NULL,
	id_billing_config int4 NOT NULL,
	id_order int8 NULL,
	id_subscriber int8 NOT NULL,
	CONSTRAINT t_billing_billing_status_check CHECK (((billing_status >= 0) AND (billing_status <= 3))),
	CONSTRAINT t_billing_pkey PRIMARY KEY (id_billing),
	CONSTRAINT fk1fb77ehq2r5rtqtyt8vwj00ix FOREIGN KEY (id_order) REFERENCES t_order(id_order),
	CONSTRAINT fk5sweug6t3ug60jnspqk1au1j0 FOREIGN KEY (id_billing_config) REFERENCES t_billing_config(id_billing_config),
	CONSTRAINT fkodos3xeabwtttio3t1jtgyjqa FOREIGN KEY (id_subscriber) REFERENCES t_subscriber(id)
);


-- public.t_billing_installments definition

-- Drop table

-- DROP TABLE t_billing_installments;

CREATE TABLE t_billing_installments (
	id_billing_installments int8 NOT NULL,
	installment_created_at date NOT NULL,
	installment_due_date date NOT NULL,
	installment_value float8 NOT NULL,
	installment_paid_value float8 NULL,
	installment_payment_date date NULL,
	installment_status int2 NOT NULL,
	installment_uuid varchar(255) NOT NULL,
	id_billing int8 NULL,
	id_subscriber int8 NOT NULL,
	CONSTRAINT t_billing_installments_installment_status_check CHECK (((installment_status >= 0) AND (installment_status <= 3))),
	CONSTRAINT t_billing_installments_pkey PRIMARY KEY (id_billing_installments),
	CONSTRAINT fkgy056sxyftgcwc1vebhbpgk FOREIGN KEY (id_billing) REFERENCES t_billing(id_billing),
	CONSTRAINT fkktpag9wkg8mtoxgl8fvq9brgb FOREIGN KEY (id_subscriber) REFERENCES t_subscriber(id)
);


-- public.t_company definition

-- Drop table

-- DROP TABLE t_company;

CREATE TABLE t_company (
	cnpj_cgc varchar(18) NOT NULL,
	inscr_estadual varchar(255) NULL,
	inscr_municipal varchar(255) NULL,
	id_customer int4 NOT NULL,
	CONSTRAINT t_company_pkey PRIMARY KEY (id_customer),
	CONSTRAINT fk6cu7e3ywa95e1p5ynnmcubu7i FOREIGN KEY (id_customer) REFERENCES t_customer(id_customer)
);


-- public.t_permission definition

-- Drop table

-- DROP TABLE t_permission;

CREATE TABLE t_permission (
	operation_name varchar(255) NOT NULL,
	user_login int8 NOT NULL,
	id_resource int4 NOT NULL,
	CONSTRAINT t_permission_operation_name_check CHECK (((operation_name)::text = ANY ((ARRAY['ADD'::character varying, 'ERASE'::character varying, 'READ'::character varying, 'UPDATE'::character varying])::text[]))),
	CONSTRAINT t_permission_pkey PRIMARY KEY (operation_name, id_resource, user_login),
	CONSTRAINT fk85g5qesqvtrxoreqb9q2vujw8 FOREIGN KEY (id_resource) REFERENCES t_resource(id_resource),
	CONSTRAINT fkbqhofa1db1hxe7l15i4od25xp FOREIGN KEY (user_login) REFERENCES t_user(id)
);

-- Grant privileges on all sequences to sas_app
GRANT ALL PRIVILEGES ON t_plan_config_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_plan_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_registration_token_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_subscriber_plan_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_subscriber_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_user_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_billing_config_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_billing_installments_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_billing_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_customer_address_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_customer_contact_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_customer_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_order_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_production_order_seq TO sas_app;
GRANT ALL PRIVILEGES ON t_resource_seq TO sas_app;

-- Grant privileges on all tables to sas_app
GRANT ALL PRIVILEGES ON t_plan TO sas_app;
GRANT ALL PRIVILEGES ON t_subscriber TO sas_app;
GRANT ALL PRIVILEGES ON t_voucher TO sas_app;
GRANT ALL PRIVILEGES ON t_plan_config TO sas_app;
GRANT ALL PRIVILEGES ON t_subscriber_plan TO sas_app;
GRANT ALL PRIVILEGES ON t_user TO sas_app;
GRANT ALL PRIVILEGES ON t_registration_token TO sas_app;
GRANT ALL PRIVILEGES ON t_billing_config TO sas_app;
GRANT ALL PRIVILEGES ON t_resource TO sas_app;
GRANT ALL PRIVILEGES ON t_customer TO sas_app;
GRANT ALL PRIVILEGES ON t_customer_address TO sas_app;
GRANT ALL PRIVILEGES ON t_customer_contact TO sas_app;
GRANT ALL PRIVILEGES ON t_individual TO sas_app;
GRANT ALL PRIVILEGES ON t_order TO sas_app;
GRANT ALL PRIVILEGES ON t_production_order TO sas_app;
GRANT ALL PRIVILEGES ON t_billing TO sas_app;
GRANT ALL PRIVILEGES ON t_billing_installments TO sas_app;
GRANT ALL PRIVILEGES ON t_company TO sas_app;
GRANT ALL PRIVILEGES ON t_permission TO sas_app;

INSERT INTO t_voucher (code,created_at,status,updated_at) VALUES ('PROMO1', '2025-07-11 13:56:24.774',0, '2025-07-11 13:56:24.774');
INSERT INTO t_voucher (code,created_at,status,updated_at) VALUES ('PROMO2', '2025-07-11 13:56:24.775',0, '2025-07-11 13:56:24.775');
INSERT INTO t_voucher (code,created_at,status,updated_at) VALUES ('PROMO3', '2025-07-11 13:56:24.776',0, '2025-07-11 13:56:24.776');
INSERT INTO t_voucher (code,created_at,status,updated_at) VALUES ('SPECIAL_PARTNER_1', '2025-07-11 13:56:24.776',0, '2025-07-11 13:56:24.776');


INSERT INTO t_plan (id,created_at,description,name,plan_key,status,updated_at) VALUES (1, '2025-07-11 13:56:24.777','','Simples','SIMPLE',0, '2025-07-11 13:56:24.777');
INSERT INTO t_plan (id,created_at,description,name,plan_key,status,updated_at) VALUES (2, '2025-07-11 13:56:24.778','','Profissional','PROFESSIONAL',0, '2025-07-11 13:56:24.778');

SELECT setval('t_plan_seq', 3);

INSERT INTO t_plan_config (id,created_at,currency,max_users,price,updated_at,plan_id) VALUES (1, '2025-07-11 13:56:24.777','BRL',1,50.0, '2025-07-11 13:56:24.777',1);
INSERT INTO t_plan_config (id,created_at,currency,max_users,price,updated_at,plan_id) VALUES (2, '2025-07-11 13:56:24.779','BRL',10,150.0, '2025-07-11 13:56:24.779',2);

SELECT setval('t_plan_config_seq', 3);

INSERT INTO t_subscriber (id,activated_at,address,country,created_at,email,name,phone,status,terms_and_conditions_accepted,updated_at,uuid) VALUES (1,null,'123 Main St','Brazil', '2025-06-14 19:38:43.764','jane.doe@example.com','Jane Doe','945767359554207',1,true, '2025-06-14 19:38:43.764','14721235-f3d5-4e7b-821a-2be971c4c0c9');

SELECT setval('t_subscriber_seq', 2);

INSERT INTO t_subscriber_plan (subscriber_plan_id,created_at,status,updated_at,subscriber_id,plan_id,voucher_id) VALUES (1, '2025-06-14 19:38:43.764',0, '2025-06-14 19:38:43.764',1,1,'PROMO1');

SELECT setval('t_subscriber_plan_seq', 2);

INSERT INTO t_registration_token (id,created_at,expires_at,token,type,used,subscriber_id,user_id) VALUES (1, '2025-06-14 19:38:43.840', '2027-05-10 06:18:43.840','bvEhbFWbofcXt1hWDarQKJysW9OVzC',0,false,1,null);

SELECT setval('t_registration_token_seq', 2);

INSERT INTO t_user (id,created_at,email,first_name,hash_password,is_admin,last_name,status,updated_at,user_name,uuid,subscriber_id) VALUES (1, '2025-06-14 19:38:51.349','jane.doe@example.com',null,'$argon2id$v=19$m=65536,t=2,p=1$hmHfK5KNNSIezsTHwkQcLw==$YaXRjs5OKuTpqSuLCpQNqziudfBpjxu1pOeOyyO3zBA=',true,null,1, '2025-06-14 19:38:51.349','jane.doe@example.com','24a0d39f-eb38-431e-98c1-4a873117d24a',1);

SELECT setval('t_user_seq', 2);


-- Billing Configuration Data
-- Insert billing configurations for different payment types and installment options

-- ONE_TIME payment configuration
insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 1,
           'Pagamento à Vista',
           'ONE_TIME',
           null,
           null,
           null );

-- INSTALLMENTS configurations (2-24 installments, monthly)
insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 2,
           'Parcelado em 2x',
           'INSTALLMENTS',
           2,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 3,
           'Parcelado em 3x',
           'INSTALLMENTS',
           3,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 4,
           'Parcelado em 4x',
           'INSTALLMENTS',
           4,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 5,
           'Parcelado em 5x',
           'INSTALLMENTS',
           5,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 6,
           'Parcelado em 6x',
           'INSTALLMENTS',
           6,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 7,
           'Parcelado em 7x',
           'INSTALLMENTS',
           7,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 8,
           'Parcelado em 8x',
           'INSTALLMENTS',
           8,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 9,
           'Parcelado em 9x',
           'INSTALLMENTS',
           9,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 10,
           'Parcelado em 10x',
           'INSTALLMENTS',
           10,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 11,
           'Parcelado em 11x',
           'INSTALLMENTS',
           11,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 12,
           'Parcelado em 12x',
           'INSTALLMENTS',
           12,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 13,
           'Parcelado em 13x',
           'INSTALLMENTS',
           13,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 14,
           'Parcelado em 14x',
           'INSTALLMENTS',
           14,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 15,
           'Parcelado em 15x',
           'INSTALLMENTS',
           15,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 16,
           'Parcelado em 16x',
           'INSTALLMENTS',
           16,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 17,
           'Parcelado em 17x',
           'INSTALLMENTS',
           17,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 18,
           'Parcelado em 18x',
           'INSTALLMENTS',
           18,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 19,
           'Parcelado em 19x',
           'INSTALLMENTS',
           19,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 20,
           'Parcelado em 20x',
           'INSTALLMENTS',
           20,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 21,
           'Parcelado em 21x',
           'INSTALLMENTS',
           21,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 22,
           'Parcelado em 22x',
           'INSTALLMENTS',
           22,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 23,
           'Parcelado em 23x',
           'INSTALLMENTS',
           23,
           1,
           'MONTH' );

insert into t_billing_config (
   id_billing_config,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 24,
           'Parcelado em 24x',
           'INSTALLMENTS',
           24,
           1,
           'MONTH' );

-- Update sequence to avoid conflicts
select setval(
   't_billing_config_seq',
   24
);


