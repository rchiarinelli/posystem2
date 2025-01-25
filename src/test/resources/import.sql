insert into t_subscriber(id_subscriber, subscriber_city,subscriber_complement,subscriber_doc,subscriber_email,subscriber_name,subscriber_number,subscriber_street,subscriber_zip_code)values (1,'NY','West Side','99999','johnsmith@brainyit.com.br','BrainyIT','4321','Main street','999999');

insert into t_resource(resource_name) values ('Res1');
insert into t_resource(resource_name) values ('Res2');
insert into t_resource(resource_name) values ('Res3');
insert into t_resource(resource_name) values ('Res4');
insert into t_resource(resource_name) values ('Res5');
insert into t_resource(resource_name) values ('Res6');
insert into t_resource(resource_name) values ('Res7');

insert into t_user (user_login,user_email,user_password,id_subscriber) values ('rchiarinelli','rchiarinelli@gmail.com','12345',1);
insert into t_user (user_login,user_email,user_password,id_subscriber) values ('mhnagaoka','mhnagaoka@gmail.com','12345',1);
insert into t_user (user_login,user_email,user_password,id_subscriber) values ('rbogre','rbogre@gmail.com','12345',1);

insert into t_permission (user_login,operation_name,id_resource) values ('rchiarinelli','READ',1);
insert into t_permission (user_login,operation_name,id_resource) values ('mhnagaoka','READ',1);
insert into t_permission (user_login,operation_name,id_resource) values ('rbogre','READ',1);

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'John Smith', 'Regular customer', 0);
insert into t_individual (id_customer, cpf) values (1, '123.456.789-00');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Mary Johnson', 'VIP customer', 0);
insert into t_individual (id_customer, cpf) values (2, '234.567.890-11');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Robert Williams', 'New customer', 0);
insert into t_individual (id_customer, cpf) values (3, '345.678.901-22');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Patricia Brown', 'Long-term customer', 0);
insert into t_individual (id_customer, cpf) values (4, '456.789.012-33');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Michael Davis', 'Premium customer', 0);
insert into t_individual (id_customer, cpf) values (5, '567.890.123-44');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Acme Corporation', 'Large enterprise client', 0);
insert into t_company (id_customer, cnpj_cgc) values (6, '12.345.678/0001-90');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'TechCorp Solutions', 'IT services provider', 0);
insert into t_company (id_customer, cnpj_cgc) values (7, '23.456.789/0001-01');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Global Industries Ltd', 'Manufacturing partner', 0);
insert into t_company (id_customer, cnpj_cgc) values (8, '34.567.890/0001-12');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'First Bank Corp', 'Financial services client', 0);
insert into t_company (id_customer, cnpj_cgc) values (9, '45.678.901/0001-23');

insert into t_customer (id_subscriber, cust_name, cust_details, cust_status) values (1, 'Retail Masters Inc', 'Retail chain customer', 0);
insert into t_company (id_customer, cnpj_cgc) values (10, '56.789.012/0001-34');



