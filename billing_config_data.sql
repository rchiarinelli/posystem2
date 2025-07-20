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
   24,
   true
);