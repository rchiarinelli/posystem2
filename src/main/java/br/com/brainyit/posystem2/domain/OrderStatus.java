/**
 * 
 */
package br.com.brainyit.posystem2.domain;

/**
 * @author Rafael Chiarinelli
 *
 */
public enum OrderStatus {

	PENDING("Pendente"),CLOSED("Fechado"),BILLED("Faturado"),CANCELLED("Cancelado"),EXECUTING("Executando"),DELIVERED("Entregue");
	
	private final String status;
	
	private OrderStatus(String c){
		this.status = c;
	}
	
	public String getStatusValue(){
		return this.status;
	}

}
