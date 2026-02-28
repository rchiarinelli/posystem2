package br.com.brainyit.posystem2.domain;

/**
 * 
 * @author rchiari
 *
 */
public enum POStatus {

	PLANNING("Em preparação"), EXECUTING("Executando"), DONE("Finalizada"), CANCELLED("Cancelada"),
	READY_TO_PRODUCE("Pronto para produzir");

	private final String status;

	private POStatus(String c) {
		this.status = c;
	}

	public String getStatusValue() {
		return this.status;
	}
}
