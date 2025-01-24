/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author leafar
 *
 */
@Entity
@Table(name="t_billing_installments")
public class Installment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9121900563716300941L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_billing_installments")
	private Long id;	
	
	@Column(name="installment_value",nullable=false)
	private Double value;
	
	@Column(name="installment_date",nullable=false)
	private Date date;
	
	@Column(name="installment_status",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private InstallmentStatus status;
	
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="id_billing",insertable=false,updatable=false)
    @Fetch(FetchMode.JOIN)
	private Billing billing;
	
    @Column(name="installment_payment_date",nullable=true)
    private Date paymentDate;
    
    @Column(name="installment_final_value",nullable=true)
    private Double finalValue;
    
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_subscriber",nullable=false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;

	
	/**
	 * 
	 */
	public Installment() {
		super();
	}

	/**
	 * @param value
	 * @param date
	 * @param status
	 */
	public Installment(Double value, Date date, InstallmentStatus status) {
		super();
		this.value = value;
		this.date = date;
		this.status = status;
	}

	/**
	 * @param value
	 * @param date
	 * @param status
	 * @param billing
	 */
	public Installment(Double value, Date date, InstallmentStatus status,
			Billing billing) {
		super();
		this.value = value;
		this.date = date;
		this.status = status;
		this.billing = billing;
	}

	


	/**
	 * @param value
	 * @param date
	 * @param status
	 * @param billing
	 * @param subscriber
	 */
	public Installment(Double value, Date date, InstallmentStatus status,
			Billing billing, Subscriber subscriber) {
		super();
		this.value = value;
		this.date = date;
		this.status = status;
		this.billing = billing;
		this.subscriber = subscriber;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public InstallmentStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(InstallmentStatus status) {
		this.status = status;
	}

	/**
	 * @return the billing
	 */
	public Billing getBilling() {
		return billing;
	}

	/**
	 * @param billing the billing to set
	 */
	public void setBilling(Billing billing) {
		this.billing = billing;
	}

	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the finalValue
	 */
	public Double getFinalValue() {
		return finalValue;
	}

	/**
	 * @param finalValue the finalValue to set
	 */
	public void setFinalValue(Double finalValue) {
		this.finalValue = finalValue;
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
