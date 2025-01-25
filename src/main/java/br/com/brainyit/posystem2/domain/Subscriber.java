/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leafar
 *
 */
@Entity
@Table(name="t_subscriber")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriber implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6709193260259588229L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_subscriber")
	private Long id;
	
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

}
