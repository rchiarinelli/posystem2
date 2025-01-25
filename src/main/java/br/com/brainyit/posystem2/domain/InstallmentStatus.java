/**
 * 
 */
package br.com.brainyit.posystem2.domain;

/**
 * @author leafar
 *
 */
public enum InstallmentStatus {

	PENDING("Pendente"),PAID("Pago");
	
	private final String status;
	
	private InstallmentStatus(String s) {
		this.status = s;
	}
	
	public String getStatusValue() {
		return this.status;
	}
	
}
