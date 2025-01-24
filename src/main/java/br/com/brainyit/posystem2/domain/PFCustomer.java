/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * @author leafar
 *
 */
@Entity(name="t_pf")
@PrimaryKeyJoinColumn(name="id_customer")
public class PFCustomer extends Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1541797716230550445L;

	@Column(name="cpf",nullable=false,length=14)
	private String cpf;
	
	@Column(name="rg",nullable=true)
	private String rg;
	
	

	/**
	 * 
	 */
	public PFCustomer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cpf
	 * @param rg
	 */
	public PFCustomer(Integer id, String name,String cpf, String rg) {
		super(id, name);
		this.cpf = cpf;
		this.rg = rg;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * @return the rg
	 */
	public String getRg() {
		return rg;
	}

	/**
	 * @param cpf the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @param rg the rg to set
	 */
	public void setRg(String rg) {
		this.rg = rg;
	}
}
