/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.brainyit.posystem2.command.Command;
import br.com.brainyit.posystem2.command.TransactionalCommand;
import br.com.brainyit.posystem2.command.impl.PerformOrderCommand;
import br.com.brainyit.posystem2.dao.OrderDAOImpl;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;
import br.com.brainyit.posystem2.util.HibernateUtils;

/**
 * @author Rafael Chiarinelli
 *
 */
@Entity
@Table(name="t_order")
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7675237026436783348L;
	
	private static Logger logger = Logger.getLogger(Order.class);

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_order")
	private Long id;
	
	@ManyToOne(targetEntity=Customer.class,fetch=FetchType.EAGER)
	@JoinColumn(name="id_customer")
	@Fetch(FetchMode.JOIN)
	private Customer customer;
	
	@Column(name="open_date",nullable=false)
	private Date openDate;
	
	@Column(name="close_date",nullable=true)
	private Date closeDate;
	
	@Column(name="order_description",nullable=false,length=255)
	private String description;
	
	@Column(name="order_price",nullable=true)
	private Double price;
	
	@Column(name="qtd_item",nullable=false)
	private Integer quantity;

	@Column(name="order_status",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private OrderStatus status;

	@Column(name="order_code",nullable=false,length=20)
	private String orderCode;
	
	@OneToMany(fetch=FetchType.LAZY,targetEntity=ProductionOrder.class)
	@JoinColumn(name="id_order")
	@Fetch(FetchMode.JOIN)
	private Set<ProductionOrder> productionOrders;
	
	@Column(name="deliver_date",nullable=true)
	private Date deliverDate;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name="id_order")
	@Fetch(FetchMode.JOIN)
	private Set<Billing> billings;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_subscriber")
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;
	
	
	@Transient
	private OrderDAOImpl dao;
	
	/**
	 * 
	 */
	public Order() {
		super();
		this.dao = new OrderDAOImpl();
	}
	
	/**
	 * 
	 * @param dao
	 */
	public Order(OrderDAOImpl dao) {
		super();
		this.dao = dao;
	}
	
	



	/**
	 * @param id
	 * @param customer
	 * @param openDate
	 * @param closeDate
	 * @param description
	 * @param price
	 * @param quantity
	 * @param status
	 * @param orderCode
	 * @param deliverDate
	 */
	public Order(Long id, Customer customer, Date openDate, Date closeDate,
			String description, Double price, Integer quantity,
			OrderStatus status, String orderCode, Date deliverDate) {
		this.id = id;
		this.customer = customer;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.status = status;
		this.orderCode = orderCode;
		this.deliverDate = deliverDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the openDate
	 */
	public Date getOpenDate() {
		return openDate;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate() {
		return closeDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @param openDate the openDate to set
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	/**
	 * @return the poCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param poCode the poCode to set
	 */
	public void setOrderCode(String poCode) {
		this.orderCode = poCode;
	}

	/**
	 * @return the productionOrders
	 */
	public Set<ProductionOrder> getProductionOrders() {
		return productionOrders;
	}

	/**
	 * @param productionOrders the productionOrders to set
	 */
	public void setProductionOrders(Set<ProductionOrder> productionOrders) {
		this.productionOrders = productionOrders;
	}

	/**
	 * @return the deliverDate
	 */
	public Date getDeliverDate() {
		return deliverDate;
	}

	/**
	 * @param deliverDate the deliverDate to set
	 */
	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	/**
	 * @return the billings
	 */
	public Set<Billing> getBillings() {
		return billings;
	}

	/**
	 * @param billings the billings to set
	 */
	public void setBillings(Set<Billing> billings) {
		this.billings = billings;
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
	/**
	 * 
	 * @param id
	 */
	public void close(Long id) {

	}
	/**
	 * 
	 * @param id
	 */
	public void cancel(Long id) {
		
		
	}
	/**
	 * 
	 * @param id
	 */
	public void bill(Long id) {
		
	}
	/**
	 * 
	 * @param order
	 * @param po
	 * @return
	 */
	public Long addPO(Order order, ProductionOrder po) {
		throw new UnsupportedOperationException("Operation not implemented.");
	}
	/**
	 * Realiza a execucao da venda, retornando o ID gerado.
	 * 
	 * @param order
	 * @return
	 * @throws BusinessException 
	 */
	public Long perform(Order order) throws BusinessException, SystemException{
		logger.debug("Performing the order.");
		Session session = HibernateUtils.getSessionFactory().getCurrentSession();
		Command performOrderCommand = new TransactionalCommand(session, new PerformOrderCommand(order,this.dao,session));
		performOrderCommand.execute();
		Long id = order.getId();
		logger.debug("Order performed");
		return id;
	}
}
