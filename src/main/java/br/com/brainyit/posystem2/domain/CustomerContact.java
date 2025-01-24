/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

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

/**
 * @author leafar
 *
 */
@Entity(name="t_customer_contact")
public class CustomerContact implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6182917470004897444L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_contact")
	private Integer id;
	
	@Column(name="name",length=45,nullable=true)
	private String name;
	
	@Column(name="email",length=45,nullable=true)
	private String email;
	
	@Column(name="office_phone_number",length=20,nullable=true)
	private String officePhoneNumber;
	
	@Column(name="mobile_phone_number",length=20,nullable=true)
	private String mobilePhoneNumber;
	
	@Column(name="contact_status",nullable=false)
	@Enumerated(EnumType.ORDINAL)
	private ContactStatus status;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_customer",insertable=false,updatable=false)
	private Customer customer;

	
	
	/**
	 * 
	 */
	public CustomerContact() {
		super();
	}
	
	

	/**
	 * @param name
	 * @param email
	 * @param officePhoneNumber
	 * @param mobilePhoneNumber
	 * @param status
	 * @param customer
	 */
	public CustomerContact(String name, String email, String officePhoneNumber,
			String mobilePhoneNumber, ContactStatus status, Customer customer) {
		super();
		this.name = name;
		this.email = email;
		this.officePhoneNumber = officePhoneNumber;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.status = status;
		this.customer = customer;
	}

	/**
	 * @param id
	 * @param name
	 * @param email
	 * @param officePhoneNumber
	 * @param mobilePhoneNumber
	 * @param status
	 * @param customer
	 */
	public CustomerContact(Integer id, String name, String email,
			String officePhoneNumber, String mobilePhoneNumber,
			ContactStatus status, Customer customer) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.officePhoneNumber = officePhoneNumber;
		this.mobilePhoneNumber = mobilePhoneNumber;
		this.status = status;
		this.customer = customer;
	}



	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the officePhoneNumber
	 */
	public String getOfficePhoneNumber() {
		return officePhoneNumber;
	}

	/**
	 * @return the mobilePhoneNumber
	 */
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	/**
	 * @return the status
	 */
	public ContactStatus getStatus() {
		return status;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param officePhoneNumber the officePhoneNumber to set
	 */
	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
	}

	/**
	 * @param mobilePhoneNumber the mobilePhoneNumber to set
	 */
	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ContactStatus status) {
		this.status = status;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
