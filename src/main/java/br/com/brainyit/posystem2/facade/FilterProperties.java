/**
 * 
 */
package br.com.brainyit.posystem2.facade;

import br.com.brainyit.filterengine.FilterCondition;

/**
 * @author rafael
 *
 */
public class FilterProperties {

	private String field;
	
	private FilterCondition filterCondition;
	
	private Object value;

	/**
	 * @param field
	 * @param filterCondition
	 * @param value
	 */
	public FilterProperties(String field, FilterCondition filterCondition,
			Object value) {
		this.field = field;
		this.filterCondition = filterCondition;
		this.value = value;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return the filterCondition
	 */
	public FilterCondition getFilterCondition() {
		return filterCondition;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @param filterCondition the filterCondition to set
	 */
	public void setFilterCondition(FilterCondition filterCondition) {
		this.filterCondition = filterCondition;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	
	
	
	
	
}
