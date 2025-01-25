/**
 * 
 */
package br.com.brainyit.posystem2.domain;

/**
 * @author leafar
 *
 */
public enum PaymentType {

	INSTALLMENTS("Parcelado"),NO_INTALLMENTS("A vista"),CREDIT_CARD("Cartão de crédito");
	
	private final String type;
	
	private PaymentType(String t){
		this.type = t;
	}
	
	public String getTypeValue(){
		return this.type;
	}
	
}
