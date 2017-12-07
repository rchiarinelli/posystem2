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
@Entity(name="t_pj")
@PrimaryKeyJoinColumn(name="id_customer")
public class PJCustomer extends Customer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5514879621621285962L;

	@Column(name="cnpj_cgc",nullable=false, length=18)
	private String cnpjCgc;
	
	@Column(name="inscr_estadual",nullable=true)
	private String inscrEstadual;
	
	@Column(name="inscr_municipal",nullable=true)
	private String inscrMunicipal;

	
	/**
	 * 
	 */
	public PJCustomer() {

	}

	/**
	 * @param id
	 * @param name
	 */
	public PJCustomer(Integer id, String name) {
		super(id, name);
	}

	/**
	 * @param id
	 */
	public PJCustomer(Integer id) {
		super(id);
	}
	
	

	/**
	 * @param cnpjCgc
	 * @param inscrEstadual
	 * @param inscrMunicipal
	 */
	public PJCustomer(Integer id, String name,String cnpjCgc, String inscrEstadual, String inscrMunicipal) {
		super(id,name);
		this.cnpjCgc = cnpjCgc;
		this.inscrEstadual = inscrEstadual;
		this.inscrMunicipal = inscrMunicipal;
	}

	/**
	 * @return the cnpjCgc
	 */
	public String getCnpjCgc() {
		return cnpjCgc;
	}

	/**
	 * @return the inscrEstadual
	 */
	public String getInscrEstadual() {
		return inscrEstadual;
	}

	/**
	 * @return the inscrMunicipal
	 */
	public String getInscrMunicipal() {
		return inscrMunicipal;
	}

	/**
	 * @param cnpjCgc the cnpjCgc to set
	 */
	public void setCnpjCgc(String cnpjCgc) {
		this.cnpjCgc = cnpjCgc;
	}

	/**
	 * @param inscrEstadual the inscrEstadual to set
	 */
	public void setInscrEstadual(String inscrEstadual) {
		this.inscrEstadual = inscrEstadual;
	}

	/**
	 * @param inscrMunicipal the inscrMunicipal to set
	 */
	public void setInscrMunicipal(String inscrMunicipal) {
		this.inscrMunicipal = inscrMunicipal;
	}
}
