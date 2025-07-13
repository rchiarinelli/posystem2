package br.com.brainyit.posystem2.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_billing_config")
public class BillingConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_config_seq")
    @SequenceGenerator(name = "billing_config_seq", sequenceName = "t_billing_config_seq", allocationSize = 1)
    @Column(name = "id_billing_config")
    private Integer id;

    @Column(name = "billing_config_name")
    private String name;

    @Column(name = "billing_config_payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "billing_config_installments_quantity", nullable = true)
    private Short installmentsQuantity;

    @Column(name = "billing_config_installments_interval", nullable = true)
    private Short installmentsInterval;

    @Column(name = "billing_config_installments_interval_unit", nullable = true)
    @Enumerated(EnumType.STRING)
    private InstallmentIntervalUnit installmentIntervalUnit;
}
