/**
 * 
 */
package br.com.brainyit.posystem2.domain;

/**
 * Contem os status possiveis de serem associados a um faturamento.
 * @author leafar
 *
 */
public enum BillingStatus {

	OPEN("Aberto"), CLOSED("Fechado");
	
	
	private final String status;
	
	private BillingStatus(String c){
		this.status = c;
	}
	
	public String getStatusValue(){
		return this.status;
	}
}
