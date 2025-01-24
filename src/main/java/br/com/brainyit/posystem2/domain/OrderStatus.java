/**
 * 
 */
package br.com.brainyit.posystem2.domain;

/**
 * @author Rafael Chiarinelli
 *
 */
public enum OrderStatus {

	OPEN("Aberto"),CLOSED("Fechado"),BILLED("Faturado"),CANCELLED("Cancelado"),EXECUTING("Executando");
	
	private final String status;
	
	private OrderStatus(String c){
		this.status = c;
	}
	
	public String getStatusValue(){
		return this.status;
	}

}
