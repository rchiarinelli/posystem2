package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_resource")
public class Resource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7759010251047999231L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_seq")
	@SequenceGenerator(name = "resource_seq", sequenceName = "t_resource_seq", allocationSize = 1)
	@Column(name = "id_resource")
	private Integer id;

	@Column(name = "resource_name", nullable = false)
	private String name;

}
