/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @author leafar
 *
 */
@Entity
@Table(name="t_subscriber")
public class Subscriber implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6709193260259588229L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_subscriber")
	private Integer id;
	
	@Column(name="subscriber_name",nullable=false)
	private String name;
	
	@Column(name="subscriber_email",nullable=false)
	private String email;
	
	@Column(name="subscriber_street",nullable=false)
	private String street;
	
	@Column(name="subscriber_complement",nullable=false)
	private String complement;
	
	@Column(name="subscriber_number",nullable=false)
	private String number;
	
	@Column(name="subscriber_zip_code",nullable=false)
	private String zipCode;
	
	@Column(name="subscriber_city",nullable=false)
	private String city;
	
	@Column(name="subscriber_doc",nullable=false)
	private String document;

	@OneToMany(fetch=FetchType.LAZY,targetEntity=User.class)
	@Column(name="id_subscriber")
	@Fetch(FetchMode.JOIN)
	private Set<User> users;

	
	
	/**
	 * 
	 */
	public Subscriber() {
	}

	/**
	 * @param id
	 */
	public Subscriber(Integer id) {
		super();
		this.id = id;
	}
	
	

	/**
	 * @param id
	 * @param name
	 * @param email
	 * @param street
	 * @param complement
	 * @param number
	 * @param zipCode
	 * @param city
	 * @param document
	 * @param users
	 */
	public Subscriber(Integer id, String name, String email, String street,
			String complement, String number, String zipCode, String city,
			String document, Set<User> users) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.street = street;
		this.complement = complement;
		this.number = number;
		this.zipCode = zipCode;
		this.city = city;
		this.document = document;
		this.users = users;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((complement == null) ? 0 : complement.hashCode());
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		Subscriber other = (Subscriber) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (complement == null) {
			if (other.complement != null)
				return false;
		} else if (!complement.equals(other.complement))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
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
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @return the complement
	 */
	public String getComplement() {
		return complement;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the document
	 */
	public String getDocument() {
		return document;
	}

	/**
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
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
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @param complement the complement to set
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(String document) {
		this.document = document;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Subscriber [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", email=");
		builder.append(email);
		builder.append(", street=");
		builder.append(street);
		builder.append(", complement=");
		builder.append(complement);
		builder.append(", number=");
		builder.append(number);
		builder.append(", zipCode=");
		builder.append(zipCode);
		builder.append(", city=");
		builder.append(city);
		builder.append(", document=");
		builder.append(document);
		builder.append(", users=");
		builder.append(users);
		builder.append("]");
		return builder.toString();
	}
}
