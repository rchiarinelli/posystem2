/**
 * 
 */
package br.com.brainyit.posystem2.facade;

import java.util.List;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * Interface usada para definir os facades usados para executar filtros
 * baseados na API {@link Filter}.
 * 
 * @author rafael
 *
 */
interface FilterFacade<RB,D> extends Facade<RB> {
	/**
	 * Faz a filtragem de dados a partir do atributo do {@link RestBean}
	 * fornecido. 
	 * @param subscriber 
	 * @param valuesMap
	 * @param
	 * @return
	 */
	public List<? extends RB> filterData(Integer subscriber, List<FilterProperties> valuesMap, String sortField);
}
