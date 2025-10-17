package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leafar
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_billing")
public class Billing implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_seq")
	@SequenceGenerator(name = "billing_seq", sequenceName = "t_billing_seq", allocationSize = 1)
	@Column(name = "id_billing")
	private Long id;

	@Builder.Default
	@Column(name = "billing_uuid", nullable = false, unique = true)
	private String uuid = UUID.randomUUID().toString();

	@ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_order")
	@Fetch(FetchMode.JOIN)
	private Order order;

	@Builder.Default
	@Column(name = "billing_calculated_value", nullable = false)
	private Double calculatedValue = 0.0;

	@Builder.Default
	@Column(name = "billing_final_value", nullable = false)
	private Double finalValue = 0.0;

	@Builder.Default
	@Column(name = "billing_total_paid_value", nullable = false)
	private Double totalPaidValue = 0.0;

	@Column(name = "billing_created_at", nullable = false)
	private LocalDate createdAt;

	@Column(name = "billing_closed_at", nullable = true)
	private LocalDate closedAt;

	@Column(name = "billing_start_date", nullable = false)
	private LocalDate startDate;

	@Column(name = "billing_last_installment_payment_date", nullable = true)
	private LocalDate lastInstallmentPaymentDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_billing_config", nullable = false)
	@Fetch(FetchMode.JOIN)
	private BillingConfig billingConfig;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_billing", nullable = false)
	@Fetch(FetchMode.JOIN)
	private List<Installment> installments;

	@Column(name = "billing_status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private BillingStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_subscriber", nullable = false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;
}
