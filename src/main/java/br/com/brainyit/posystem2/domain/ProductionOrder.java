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
 * @author rchiari
 *
 */
@Entity
@Table(name="t_production_order")
public class ProductionOrder implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2038714951347635662L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_po")
	private Long id;
	
	@ManyToOne(targetEntity=Order.class,fetch=FetchType.EAGER)
	@JoinColumn(name="id_order")
	@Fetch(FetchMode.JOIN)
	private Order order;
	
	@Column(name="po_number",nullable=false)
	private Integer poNumber;
	
	@Column(name="sketch_number",nullable=true,length=10)
	private String sketchNumber;
	
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



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the request
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return the poNumber
	 */
	public Integer getPoNumber() {
		return poNumber;
	}

	/**
	 * @return the sketchNumber
	 */
	public String getSketchNumber() {
		return sketchNumber;
	}

	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * @return the qtd
	 */
	public Integer getQtd() {
		return qtd;
	}

	/**
	 * @return the deliverDate
	 */
	public Date getDeliverDate() {
		return deliverDate;
	}

	/**
	 * @return the status
	 */
	public POStatus getStatus() {
		return status;
	}

	/**
	 * @return the estimatedTime
	 */
	public Double getEstimatedTime() {
		return estimatedTime;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param order the request to set
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * @param poNumber the poNumber to set
	 */
	public void setPoNumber(Integer poNumber) {
		this.poNumber = poNumber;
	}

	/**
	 * @param sketchNumber the sketchNumber to set
	 */
	public void setSketchNumber(String sketchNumber) {
		this.sketchNumber = sketchNumber;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @param qtd the qtd to set
	 */
	public void setQtd(Integer qtd) {
		this.qtd = qtd;
	}

	/**
	 * @param deliverDate the deliverDate to set
	 */
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(POStatus status) {
		this.status = status;
	}

	/**
	 * @param estimatedTime the estimatedTime to set
	 */
	public void setEstimatedTime(Double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	/**
	 * @return the openDate
	 */
	public Date getOpenDate() {
		return openDate;
	}

	/**
	 * @param openDate the openDate to set
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
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
