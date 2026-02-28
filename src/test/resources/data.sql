insert into t_subscriber (
   id,
   uuid,
   name,
   email
) values ( 1,
           '123e4567-e89b-12d3-a456-426614174000',
           'BrainyIT',
           'johnsmith@brainyit.com.br' );

insert into t_subscriber (
   id,
   uuid,
   name,
   email
) values ( 2,
           '123e4567-e89b-12d3-a456-426614174001',
           'Jane Smith',
           'janesmith@brainyit.com.br' );

insert into t_subscriber (
   id,
   uuid,
   name,
   email
) values ( 3,
           '123e4567-e89b-12d3-a456-426614174002',
           'Peter Parker',
           'peterparker@brainyit.com.br' );

insert into t_subscriber (
   id,
   uuid,
   name,
   email
) values ( 4,
           '123e4567-e89b-12d3-a456-426614174003',
           'Tony Stark',
           'tonystark@brainyit.com.br' );

select setval(
   't_subscriber_seq',
   5
);

insert into t_resource (
   id_resource,
   resource_name
) values ( 1,
           'Res1' );
insert into t_resource (
   id_resource,
   resource_name
) values ( 2,
           'Res2' );
insert into t_resource (
   id_resource,
   resource_name
) values ( 3,
           'Res3' );
insert into t_resource (
   id_resource,
   resource_name
) values ( 4,
           'Res4' );
insert into t_resource (
   id_resource,
   resource_name
) values ( 5,
           'Res5' );
insert into t_resource (
   id_resource,
   resource_name
) values ( 6,
           'Res6' );
insert into t_resource (
   id_resource,
   resource_name
) values ( 7,
           'Res7' );

select setval(
   't_resource_seq',
   8
);

insert into t_user (
   id,
   uuid,
   first_name,
   last_name,
   email,
   user_name,
   created_at,
   updated_at,
   subscriber_id
) values ( 1,
           '123e4567-e89b-12d3-a456-426614174000',
           'John',
           'Smith',
           'johnsmith@brainyit.com.br',
           'johnsmith@brainyit.com.br',
           now(),
           now(),
           1 );
insert into t_user (
   id,
   uuid,
   first_name,
   last_name,
   email,
   user_name,
   created_at,
   updated_at,
   subscriber_id
) values ( 2,
           '123e4567-e89b-12d3-a456-426614174001',
           'Jane',
           'Smith',
           'janesmith@brainyit.com.br',
           'janesmith@brainyit.com.br',
           now(),
           now(),
           1 );

select setval(
   't_user_seq',
   3
);

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 1,
           1,
           'John Smith',
           'Regular customer',
           0 );
insert into t_individual (
   id_customer,
   cpf
) values ( 1,
           '123.456.789-00' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 2,
           1,
           'Mary Johnson',
           'VIP customer',
           0 );
insert into t_individual (
   id_customer,
   cpf
) values ( 2,
           '234.567.890-11' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 3,
           1,
           'Robert Williams',
           'New customer',
           0 );
insert into t_individual (
   id_customer,
   cpf
) values ( 3,
           '345.678.901-22' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 4,
           1,
           'Patricia Brown',
           'Long-term customer',
           0 );
insert into t_individual (
   id_customer,
   cpf
) values ( 4,
           '456.789.012-33' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 5,
           1,
           'Michael Davis',
           'Premium customer',
           0 );
insert into t_individual (
   id_customer,
   cpf
) values ( 5,
           '567.890.123-44' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 6,
           1,
           'Acme Corporation',
           'Large enterprise client',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 6,
           '12.345.678/0001-90' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 7,
           1,
           'TechCorp Solutions',
           'IT services provider',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 7,
           '23.456.789/0001-01' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 8,
           1,
           'Global Industries Ltd',
           'Manufacturing partner',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 8,
           '34.567.890/0001-12' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 9,
           1,
           'First Bank Corp',
           'Financial services client',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 9,
           '45.678.901/0001-23' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 10,
           1,
           'Retail Masters Inc',
           'Retail chain customer',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 10,
           '56.789.012/0001-34' );


insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 11,
           2,
           'Another Customer',
           'Another customer',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 11,
           '67.890.123/0001-45' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 12,
           2,
           'Patricia Customer',
           'Patricia customer',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 12,
           '78.901.234/0001-56' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 13,
           2,
           'Mary Customer',
           'Mary customer',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 13,
           '89.012.345/0001-67' );


insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 14,
           2,
           'John Customer',
           'John customer',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 14,
           '90.123.456/0001-78' );


insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 15,
           3,
           'Mary Jane Watson',
           'Mary Jane Watson',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 15,
           '90.123.456/0001-78' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 16,
           3,
           'Harry Osborn',
           'Harry Osborn',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 16,
           '90.123.456/0001-79' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 17,
           3,
           'Gwen Stacy',
           'Gwen Stacy',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 17,
           '90.123.456/0001-80' );

insert into t_customer (
   id_customer,
   id_subscriber,
   cust_name,
   cust_details,
   cust_status
) values ( 18,
           4,
           'Pepper Potts',
           'Pepper Potts',
           0 );
insert into t_company (
   id_customer,
   cnpj_cgc
) values ( 18,
           '90.123.456/0001-80' );


select setval(
   't_customer_seq',
   19
);


-- Billing configurations test data
insert into t_billing_config (
   id_billing_config,
   id_subscriber,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 1,
           1,
           'Pagamento à Vista',
           'ONE_TIME',
           1,
           0,
           'MONTH' );
insert into t_billing_config (
   id_billing_config,
   id_subscriber,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 2,
           1,
           'Parcelado em 2x',
           'INSTALLMENTS',
           2,
           1,
           'MONTH' );
insert into t_billing_config (
   id_billing_config,
   id_subscriber,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 3,
           1,
           'Parcelado em 3x',
           'INSTALLMENTS',
           3,
           1,
           'MONTH' );
insert into t_billing_config (
   id_billing_config,
   id_subscriber,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 4,
           1,
           'Parcelado em 6x',
           'INSTALLMENTS',
           6,
           1,
           'MONTH' );
insert into t_billing_config (
   id_billing_config,
   id_subscriber,
   billing_config_name,
   billing_config_payment_type,
   billing_config_installments_quantity,
   billing_config_installments_interval,
   billing_config_installments_interval_unit
) values ( 5,
           1,
           'Parcelado em 12x',
           'INSTALLMENTS',
           12,
           1,
           'MONTH' );

-- First, create a billing configuration for 7 monthly installments if it doesn't exist
INSERT INTO t_billing_config (
    id_billing_config,
    id_subscriber,
    billing_config_name,
    billing_config_payment_type,
    billing_config_installments_quantity,
    billing_config_installments_interval,
    billing_config_installments_interval_unit
) VALUES (
    25,  -- Next available ID after the existing 24 configs
    4,   -- Subscriber ID for Tony Stark (customer 18's subscriber)
    'Parcelado em 7x',
    'INSTALLMENTS',
    7,
    1,
    'MONTH'
);

-- Update sequence to avoid conflicts
SELECT setval('t_billing_config_seq', 25);

-- Create the order in BILLED status
INSERT INTO t_order (
    id_order,
    id_customer,
    id_subscriber,
    order_description,
    order_status,  -- 2 = BILLED (enum ordinal)
    order_code,
    order_price,
    qtd_item,
    open_date,
    planned_deliver_date
) VALUES (
    2,  -- Next available order ID
    18, -- Customer ID for Pepper Potts
    4,  -- Subscriber ID for Tony Stark
    'Custom Product Order - 7 Installments',
    2,  -- BILLED status (ordinal value)
    'ORD-2024-001',
    3500.00,  -- Order value
    1.0,      -- Quantity
    CURRENT_DATE - INTERVAL '3 months',
    CURRENT_DATE - INTERVAL '1 month'
);

-- Update order sequence
SELECT setval('t_order_seq', 3);

-- Create the billing record
INSERT INTO t_billing (
    id_billing,
    billing_calculated_value,
    billing_closed_at,
    billing_created_at,
    billing_start_date,
    billing_last_installment_payment_date,
    billing_final_value,
    billing_total_paid_value,
    billing_status,  -- 0 = OPEN (enum ordinal)
    billing_uuid,
    id_billing_config,
    id_order,
    id_subscriber
) VALUES (
    1,  -- Billing ID
    3500.00,  -- Calculated value
    NULL,     -- Not closed yet
    CURRENT_DATE - INTERVAL '2 months',  -- Created 2 months ago
    CURRENT_DATE - INTERVAL '2 months',  -- Start date 2 months ago
    CURRENT_DATE + INTERVAL '5 months',  -- Last installment due in 5 months
    3500.00,  -- Final value
    0.00,     -- No payments yet
    0,        -- OPEN status (ordinal value)
    gen_random_uuid()::text,
    25,       -- Billing config ID for 7x installments
    2,        -- Order ID
    4         -- Subscriber ID
);

-- Update billing sequence
SELECT setval('t_billing_seq', 2);

-- Create 7 monthly installments
-- Installment 1 (2 months ago - overdue)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,  -- 0 = PENDING (enum ordinal)
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    1,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE - INTERVAL '2 months',
    500.00,  -- 3500 / 7 = 500 per installment
    NULL,
    NULL,
    0,  -- PENDING status
    gen_random_uuid()::text,
    1,  -- Billing ID
    4   -- Subscriber ID
);

-- Installment 2 (1 month ago - overdue)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    2,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE - INTERVAL '1 month',
    500.00,
    NULL,
    NULL,
    0,
    gen_random_uuid()::text,
    1,
    4
);

-- Installment 3 (current month - due now)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    3,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE,
    500.00,
    NULL,
    NULL,
    0,
    gen_random_uuid()::text,
    1,
    4
);

-- Installment 4 (next month)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    4,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE + INTERVAL '1 month',
    500.00,
    NULL,
    NULL,
    0,
    gen_random_uuid()::text,
    1,
    4
);

-- Installment 5 (2 months from now)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    5,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE + INTERVAL '2 months',
    500.00,
    NULL,
    NULL,
    0,
    gen_random_uuid()::text,
    1,
    4
);

-- Installment 6 (3 months from now)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    6,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE + INTERVAL '3 months',
    500.00,
    NULL,
    NULL,
    0,
    gen_random_uuid()::text,
    1,
    4
);

-- Installment 7 (4 months from now)
INSERT INTO t_billing_installments (
    id_billing_installments,
    installment_created_at,
    installment_due_date,
    installment_value,
    installment_paid_value,
    installment_payment_date,
    installment_status,
    installment_uuid,
    id_billing,
    id_subscriber
) VALUES (
    7,
    CURRENT_DATE - INTERVAL '2 months',
    CURRENT_DATE + INTERVAL '4 months',
    500.00,
    NULL,
    NULL,
    0,
    gen_random_uuid()::text,
    1,
    4
);

-- Update installments sequence
SELECT setval('t_billing_installments_seq', 8);

-- Work schedule test data
-- Standard 5-day work week (Monday to Friday), 8 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber
) values ( 1,
           'ACTIVE',
           2,  -- Monday
           6,  -- Friday
           '08:00:00',  -- Start time
           '17:00:00',  -- End time
           480, -- 8 hours
           1 ); -- Subscriber ID

-- Standard 6-day work week (Monday to Saturday), 8 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber
) values ( 2,
           'ACTIVE',
           2,  -- Monday
           7,  -- Saturday
           '08:00:00',  -- Start time
           '17:00:00',  -- End time
           480, -- 8 hours
           1 ); -- Subscriber ID

-- Full 7-day work week (Sunday to Saturday), 8 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber
) values ( 3,
           'ACTIVE',
           1,  -- Sunday
           7,  -- Saturday
           '08:00:00',  -- Start time
           '17:00:00',  -- End time
           480, -- 8 hours
           1 ); -- Subscriber ID

-- Part-time schedule (Monday to Friday), 4 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber
) values ( 4,
           'ACTIVE',
           2,  -- Monday
           6,  -- Friday
           '09:00:00',  -- Start time
           '13:00:00',  -- End time (4 hours)
           240, -- 4 hours
           1 ); -- Subscriber ID

-- Extended schedule (Monday to Friday), 10 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber
) values ( 5,
           'ACTIVE',
           2,  -- Monday
           6,  -- Friday
           '07:00:00',  -- Start time
           '18:00:00',  -- End time (11 hours with 1 hour break)
           600, -- 10 hours
           1 ); -- Subscriber ID

-- Inactive schedule (Monday to Friday), 8 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber  
) values ( 6,
           'INACTIVE',
           2,  -- Monday
           6,  -- Friday
           '08:00:00',  -- Start time
           '17:00:00',  -- End time
           480, -- 8 hours
           1 ); -- Subscriber ID

-- Weekend schedule (Saturday to Sunday), 6 hours per day
insert into t_work_schedule (
   id_work_schedule,
   status,
   week_start_day,
   week_end_day,
   start_time,
   end_time,
   shift_duration_in_minutes,
   id_subscriber
) values ( 7,
           'ACTIVE',
           7,  -- Saturday
           1,  -- Sunday
           '10:00:00',  -- Start time
           '16:00:00',  -- End time (6 hours)
           360, -- 6 hours
           1 ); -- Subscriber ID

select setval(
   't_work_schedule_seq',
   8
);

-- Work resource test data
-- Lathe machine
insert into t_work_resource (
   id_work_resource,
   name,
   description,
   type,
   status,
   setup_time_in_minutes,
   cost_per_hour,
   id_work_schedule,
   id_subscriber
) values ( 1,
           'Lathe',
           'CNC Lathe Machine for precision turning operations',
           'MACHINE',
           'ACTIVE',
           30,  -- 30 minutes setup time
           75.00,  -- $75.00 per hour
           1, -- Standard 5-day work week
           1 ); -- Subscriber ID

-- Drill machine
insert into t_work_resource (
   id_work_resource,
   name,
   description,
   type,
   status,
   setup_time_in_minutes,
   cost_per_hour,
   id_work_schedule,
   id_subscriber
) values ( 2,
           'Drill',
           'Industrial drill press for drilling operations',
           'MACHINE',
           'ACTIVE',
           15,  -- 15 minutes setup time
           50.00,  -- $50.00 per hour
           1, -- Standard 5-day work week
           1 ); -- Subscriber ID

-- Bending machine
insert into t_work_resource (
   id_work_resource,
   name,
   description,
   type,
   status,
   setup_time_in_minutes,
   cost_per_hour,
   id_work_schedule,
   id_subscriber
) values ( 3,
           'Bending',
           'Hydraulic press brake for metal bending',
           'MACHINE',
           'ACTIVE',
           20,  -- 20 minutes setup time
           85.00,  -- $85.00 per hour
           1, -- Standard 5-day work week
           1 ); -- Subscriber ID

-- Welding machine
insert into t_work_resource (
   id_work_resource,
   name,
   description,
   type,
   status,
   setup_time_in_minutes,
   cost_per_hour,
   id_work_schedule,
   id_subscriber
) values ( 4,
           'Welding',
           'MIG/TIG welding station for joining metal parts',
           'MACHINE',
           'ACTIVE',
           25,  -- 25 minutes setup time
           65.00,  -- $65.00 per hour
           1, -- Standard 5-day work week
           1 ); -- Subscriber ID

-- Saw machine
insert into t_work_resource (
   id_work_resource,
   name,
   description,
   type,
   status,
   setup_time_in_minutes,
   cost_per_hour,
   id_work_schedule,
   id_subscriber
) values ( 5,
           'Saw',
           'Band saw for cutting metal and other materials',
           'MACHINE',
           'ACTIVE',
           10,  -- 10 minutes setup time
           45.00,  -- $45.00 per hour
           1, -- Standard 5-day work week
           1 ); -- Subscriber ID

select setval(
   't_work_resource_seq',
   6
);