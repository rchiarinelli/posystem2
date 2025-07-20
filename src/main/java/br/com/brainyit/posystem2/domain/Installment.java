/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Entity
@Table(name = "t_billing_installments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Installment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9121900563716300941L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_installments_seq")
	@SequenceGenerator(name = "billing_installments_seq", sequenceName = "t_billing_installments_seq", allocationSize = 1)
	@Column(name = "id_billing_installments")
	private Long id;

	@Builder.Default
	@Column(name = "installment_uuid", nullable = false)
	private String uuid = UUID.randomUUID().toString();

	@Builder.Default
	@Column(name = "installment_value", nullable = false)
	private Double dueValue = 0.0;

	@Column(name = "installment_due_date", nullable = false)
	private LocalDate dueDate;

	@Column(name = "installment_payment_date", nullable = true)
	private LocalDate paymentDate;

	@Builder.Default
	@Column(name = "installment_paid_value", nullable = true)
	private Double paidValue = 0.0;

	@Builder.Default
	@Column(name = "installment_created_at", nullable = false)
	private LocalDate createdAt = LocalDate.now();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_billing", insertable = false, updatable = false)
	@Fetch(FetchMode.JOIN)
	private Billing billing;

	@Column(name = "installment_status", nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private InstallmentStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_subscriber", nullable = false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;

}
