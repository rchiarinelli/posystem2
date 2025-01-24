/**
 * 
 */
package br.com.brainyit.filterengine;

/**
 * @author Rafael Chiarinelli
 *
 */
public enum FilterCondition {

	EQUALS("="),NOT_EQUALS("<>"),LIKE("like"),BETWEEN("between"),IN("in");
	
	private final String cond;
	
	FilterCondition(String c){
		this.cond = c;
	}
	
	public String getArgValue(){
		return this.cond;
	}
	
}
