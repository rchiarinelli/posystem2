package br.com.brainyit.posystem2.domain;
/**
 * 
 * @author rchiari
 *
 */
public enum POStatus {
	
	PLANNING("Em planejamento"),EXECUTING("Executando"),DONE("Finalizada");
	
	private final String status;
	
	private POStatus(String c){
		this.status = c;
	}
	
	public String getStatusValue(){
		return this.status;
	}
}
