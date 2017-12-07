/**
 * 
 */
package br.com.brainyit.filterengine;

import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe cont&ecirc;m os dados aplicaveis a uma determinada
 * classe persistente, setando o {@link Class} dessa classe
 * e os argumentos aplic&aacute;veis da mesma.  
 * 
 * @author Rafael Chiarinelli
 */
public class Filter {

	public enum OrderByType {
		ASCENDING("asc"),DESCENDING("desc");
		
		private final String value;
		
		private OrderByType(String s) {
			value = s;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	private Class<?> clazz;
	
	private List<FilterArgument> arguments;
	
	private String[] orderByFields;
	
	private OrderByType orderByType;
	
	/**
	 * 
	 */
	public Filter() {
		super();
		this.arguments = new ArrayList<FilterArgument>();
	}


	/**
	 * @return the clazz
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	/**
	 * Adiciona uma nova condi&ccedil;&atilde;o ao filtro.
	 * 
	 * @param cond condicao do filtro
	 * @param field campo a ser aplicado o filtro
	 * @param value valor do filtro
	 * @param valueType tipo do valor
	 */
	@Deprecated
	public Filter addArgument(FilterCondition cond, String field,Object value,ValueType valueType){
		return this.addArgument(cond, field, new Object[]{value},valueType);
	}
	
	/**
	 * Adiciona uma nova condi&ccedil;&atilde;o ao filtro.
	 * 
	 * @param cond condicao do filtro
	 * @param field campo a ser aplicado o filtro
	 * @param value valores do filtro
	 * @param valueType tipo do valor
	 */
	@Deprecated
	public Filter addArgument(FilterCondition cond, String field,Object[] values,ValueType valueType){
		this.arguments.add(new FilterArgument(cond, field, values, valueType));
		return this;
	}	
	/**
	 * Adiciona uma nova condi&ccedil;&atilde;o ao filtro.
	 * 
	 * @param cond condicao do filtro
	 * @param field campo a ser aplicado o filtro
	 * @param value valor do filtro
	 */
	public Filter addArgument(FilterCondition cond, String field,Object value){
		return this.addArgument(cond, field, new Object[]{value});
	}
	
	/**
	 * Adiciona uma nova condi&ccedil;&atilde;o ao filtro.
	 * 
	 * @param cond condicao do filtro
	 * @param field campo a ser aplicado o filtro
	 * @param value valores do filtro
	 */
	public Filter addArgument(FilterCondition cond, String field,Object[] values){
		this.arguments.add(new FilterArgument(cond, field, values));
		return this;
	}
	/**
	 * Adiciona o criterio de ordena&ccedil;&atilde;o e o tipo, se &eacute; ascendente ou descendente.
	 * 
	 * @param fieldNames
	 * @param type
	 * @return
	 */
	public Filter addOrderBy(String[] fieldNames,OrderByType type) {
		this.orderByFields = fieldNames;
		this.orderByType = type;
		return this;
	}
	/**
	 * 
	 * @return
	 */
	public List<FilterArgument> getArguments(){
		return this.arguments;
	}


	/**
	 * @return the orderByFields
	 */
	public String[] getOrderByFields() {
		return orderByFields;
	}


	/**
	 * @return the orderByType
	 */
	public OrderByType getOrderByType() {
		return orderByType;
	}
	
	
	
}
