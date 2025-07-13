/**
 * 
 */
package br.com.brainyit.posystem2.domain;

/**
 * @author leafar
 *
 */
public enum PaymentType {

	INSTALLMENTS("Parcelado"), ONE_TIME("A vista");

	private final String type;

	private PaymentType(String t) {
		this.type = t;
	}

	public String getTypeValue() {
		return this.type;
	}

}
