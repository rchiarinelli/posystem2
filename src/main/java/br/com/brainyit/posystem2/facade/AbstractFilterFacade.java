/**
 * 
 */
package br.com.brainyit.posystem2.facade;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.filterengine.Filter.OrderByType;
import br.com.brainyit.filterengine.FilterCondition;
import br.com.brainyit.posystem2.domain.Domain;


/**
 * Classe base para implementação dos {@link FilterFacade}.Para ter acesso aos métodos base, utilize essa classe.
 * 
 * @author rafael
 *
 */
public abstract class AbstractFilterFacade<RB,D> implements FilterFacade<RB,D> {

	private static final String RB_CUSTOMER_NAME = "name";
	private static final String RB_CUSTOMER_IDENT = "custIdent";
	private static final String RB_CUSTOMER_CPF = "cpf";
	private static final String RB_CUSTOMER_CNPJ_CGC = "cnpjCgc";
	private static final String RB_CUSTOMER_CUSTOMER_STATUS = "customerStatus";
	/**
	 * Map com os campos "externos" e os reais, para ter um de-para 
	 * entre os mesmos. Usado para não expor os campos do banco de dados nos servicos.
	 */
	private static final Map<String, String[]> FILTER_FIELDS;
	
	static {
		FILTER_FIELDS = new HashMap<String, String[]>();
		FILTER_FIELDS.put(RB_CUSTOMER_NAME, new String[]{"name"});
		FILTER_FIELDS.put(RB_CUSTOMER_IDENT, new String[]{"cnpjCgc","cpf"});
		FILTER_FIELDS.put(RB_CUSTOMER_CNPJ_CGC, new String[]{"cnpjCgc"});
		FILTER_FIELDS.put(RB_CUSTOMER_CPF, new String[]{"cpf"});
		FILTER_FIELDS.put(RB_CUSTOMER_CUSTOMER_STATUS, new String[]{"status"});
	}
	
	/**
	 * Recupera o nome real do atributo da classe de dominio,
	 * de acordo bom o que foi definido Map FILTER_FIELDS.
	 * 
	 * @param rbFieldName
	 * @return
	 */
	protected String[] getRealField(String rbFieldName) {
		if (rbFieldName!=null) {
			String[] fields = FILTER_FIELDS.get(rbFieldName.substring(rbFieldName.indexOf("-")+1));
			return fields;
		}
		return null;
	}
	/**
	 * Define o tipo da ordenação do campo, de acordo com o valor
	 * de rbFieldName
	 * 
	 * @param rbFieldName
	 * @return
	 */
	protected OrderByType getOrderType(String rbFieldName) {
		OrderByType orderType = OrderByType.ASCENDING;
		if (rbFieldName.startsWith("-")) {
			orderType = OrderByType.DESCENDING;
		}
		return orderType;
	}
	
	/**
	 * 
	 * @param subscriber
	 * @param filterPropsList
	 * @param sortField
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected Filter buildFilter(Integer subscriber, List<FilterProperties> filterPropsList, String sortField) {
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		Type[] actuallyTypeArgs = parameterizedType.getActualTypeArguments();
		Filter filter = new Filter();
		Class actuallyType = null;
		for (int i = 0; i < actuallyTypeArgs.length; i++) {
			actuallyType = (Class) actuallyTypeArgs[i];
			if (Domain.class.isAssignableFrom(actuallyType)){
				filter.setClazz(actuallyType);
				break;
			}
		}
		filter.addArgument(FilterCondition.EQUALS, "subscriber.id", subscriber);
		if (filterPropsList!=null) {
			for (FilterProperties filterProps : filterPropsList) {
				//Caso o metodo getRealField retornar null
				if (this.getRealField(filterProps.getField())==null) {
					//Usar a propria chave como nome do campo
					filter.addArgument(filterProps.getFilterCondition(), filterProps.getField(), filterProps.getValue());
				} else {
					filter.addArgument(filterProps.getFilterCondition(), this.getRealField(filterProps.getField())[0], filterProps.getValue());					
				}
			}
		}
		String[] realSortFields = this.getRealField(sortField);
		if (realSortFields!=null) {
			filter.addOrderBy(realSortFields, this.getOrderType(sortField));
		}
		return filter;
	}
	
}
