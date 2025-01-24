/**
 * 
 */
package br.com.brainyit.posystem2.dao;

import java.util.List;

import org.hibernate.Query;

import br.com.brainyit.filterengine.Filter;

/**
 * Classe com alguns metodos espeficicos da aplicação.
 * 
 * @author rafael
 *
 */
public interface DAO<D> {
	
	/**
	 * Executa o objeto {@link Query} que é gerado pelo {@link Filter} 
	 * fornecido via parametro.
	 * 
	 * @param filter filter que será usado para gerar o objeto Query
	 * @return o resultado da busca
	 */
	public List<? extends D> executeFilter(Filter filter);

}
