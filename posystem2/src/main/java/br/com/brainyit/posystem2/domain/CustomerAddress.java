/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Entity(name="t_customer_address")
public class CustomerAddress implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8552510435435455194L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_address")
	private int id;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_customer",insertable=false,updatable=false)
	private Customer customer;
	
	@Column(name="street_name",length=60,nullable=true)
	private String street;
	
	@Column(name="number",nullable=true)
	private Integer number;
	
	@Column(name="complement",length=45,nullable=true)
	private String complement;
	
	@Column(name="city",length=45,nullable=true)
	private String city;
	
	@Column(name="state",length=3,nullable=true)
	private String state;
	
	@Column(name="zipcode",length=15,nullable=true)
	private String zipCode;
	
	@Column(name="country",length=3,nullable=true)
	private String country;
	
	

	/**
	 * 
	 */
	public CustomerAddress() {
		super();
	}
	
	

	/**
	 * @param customer
	 * @param street
	 * @param number
	 * @param complement
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param country
	 */
	public CustomerAddress(Customer customer, String street, Integer number,
			String complement, String city, String state, String zipCode,
			String country) {
		super();
		this.customer = customer;
		this.street = street;
		this.number = number;
		this.complement = complement;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}



	/**
	 * @param id
	 * @param customer
	 * @param street
	 * @param number
	 * @param complement
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param country
	 */
	public CustomerAddress(int id, Customer customer, String street,
			Integer number, String complement, String city, String state,
			String zipCode, String country) {
		super();
		this.id = id;
		this.customer = customer;
		this.street = street;
		this.number = number;
		this.complement = complement;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.country = country;
	}

	


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if ((obj==null) || !(obj instanceof CustomerAddress)) {
			return false;
		}
		CustomerAddress addr = (CustomerAddress) obj;
		return (addr.getId()==null? false : this.id == addr.getId().intValue()); 
	}



	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @return the complement
	 */
	public String getComplement() {
		return complement;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @param complement the complement to set
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
}
