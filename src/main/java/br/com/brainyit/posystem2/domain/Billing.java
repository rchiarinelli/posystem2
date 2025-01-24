/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author leafar
 *
 */
@Entity
@Table(name="t_billing")
public class Billing implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6959462450613891534L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_billing")
	private Long id;
	
	@ManyToOne(targetEntity=Order.class,fetch=FetchType.EAGER)
	@JoinColumn(name="id_order")
	@Fetch(FetchMode.JOIN)
	private Order order;
	
	@Column(name="billing_final_value",nullable=false)
	private Double finalValue;
	
	@Column(name="billing_date",nullable=false)
	private Date date;
	
	@Column(name="billing_payment_type",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private PaymentType paymentType;

	@OneToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@JoinColumn(name="id_billing",nullable=false)
	@Fetch(FetchMode.JOIN)
	private Set<Installment> installments;
	
	@Column(name="billing_status",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private BillingStatus status;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_subscriber",nullable=false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;

	
	/**
	 * 
	 */
	public Billing() {
		super();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return the finalValue
	 */
	public Double getFinalValue() {
		return finalValue;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @return the installments
	 */
	public Set<Installment> getInstallments() {
		return installments;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @param finalValue the finalValue to set
	 */
	public void setFinalValue(Double finalValue) {
		this.finalValue = finalValue;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @param installments the installments to set
	 */
	public void setInstallments(Set<Installment> installments) {
		this.installments = installments;
	}

	/**
	 * @return the status
	 */
	public BillingStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(BillingStatus status) {
		this.status = status;
	}

	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	
	
}
