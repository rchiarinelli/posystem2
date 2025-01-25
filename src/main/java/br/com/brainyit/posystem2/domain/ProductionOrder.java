/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Date;

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
	/**
	 * 
	 */
	private static final long serialVersionUID = 2038714951347635662L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_po")
	private Long id;
	
	@ManyToOne(targetEntity=Order.class,fetch=FetchType.EAGER)
	@JoinColumn(name="id_order")
	@Fetch(FetchMode.JOIN)
	private Order order;
	
	@Column(name="po_number",nullable=false)
	private Integer poNumber;
	
	@Column(name="description",nullable=true,length=255)
	private String description ;
	
	@Column(name="due_date",nullable=false)
	private Date dueDate;
	
	@Column(name="qtd_pieces",nullable=true)
	private Integer qtd;
	
	@Column(name="deliver_date",nullable=true)
	private Date deliverDate;
	
	@Column(name="po_status",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private POStatus status;
	
	@Column(name="estimated_time",nullable=true)
	private Double estimatedTime;
	
	@Column(name="open_date",nullable=false)
	private Date openDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_subscriber",nullable=false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;

	
}
