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

select setval(
   't_subscriber_seq',
   3
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


select setval(
   't_customer_seq',
   17
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