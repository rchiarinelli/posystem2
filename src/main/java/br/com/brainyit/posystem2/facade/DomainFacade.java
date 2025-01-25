/**
 * 
 */
package br.com.brainyit.posystem2.facade;

import java.io.Serializable;
import java.util.List;

/**
 * Interface usada para definir os metodos basicos dos Domain facades, usados
 * para fazer a ponte entre os rest beans e as classes de dominio.
 * 
 * @author rafael
 *
 */
public interface DomainFacade<RB, D> extends Facade<RB> {
	/**
	 * 
	 * @return
	 */
	public List<? extends RB> list();
	
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public RB get(Serializable identifier);

}
