/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.time.LocalDate;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rchiari
 *
 */
@Entity
@Table(name="t_production_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOrder implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_po")
	private Long id;
	
	@ManyToOne(targetEntity=Order.class,fetch=FetchType.EAGER)
	@JoinColumn(name="id_order")
	@Fetch(FetchMode.JOIN)
	private Order order;
	
	@Column(name="po_number", nullable=false, unique = true, length = 100)
	private String poNumber;
	
	@Column(name="description",nullable=true,length=255)
	private String description ;
	
	@Builder.Default
	@Column(name="qtd_pieces",nullable=true)
	private Integer qtd = 0;
	
	@Column(name="deliver_date",nullable=true)
	private LocalDate estimatedDueDate;

	@Column(name="actual_delivery_date",nullable=true)
	private LocalDate actualDeliveryDate;

	@Column(name="po_status",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private POStatus status;
	
	@Builder.Default
	@Column(name="estimated_time",nullable=false)
	private Double estimatedTime = 0.0;
	
	@Column(name="open_date",nullable=false)
	private LocalDate openDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_subscriber",nullable=false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;
	
}
