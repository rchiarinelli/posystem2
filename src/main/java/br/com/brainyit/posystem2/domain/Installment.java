/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Date;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author leafar
 *
 */
@Entity
@Table(name="t_billing_installments")
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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	
}
