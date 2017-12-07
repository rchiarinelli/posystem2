/**
 * 
 */
package br.com.brainyit.filterengine;

/**
 * @author Rafael Chiarinelli
 *
 */
@Deprecated
public enum ValueType {

	STRING("\'"),NUMBER(""),DATE("\'");
	
	private final String valueChar;
	
	ValueType(String c){
		this.valueChar = c;
	}
	
	public String getValueChar(){
		return this.valueChar;
	}
		
}
