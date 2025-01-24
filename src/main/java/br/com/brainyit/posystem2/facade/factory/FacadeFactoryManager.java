/**
 * 
 */
package br.com.brainyit.posystem2.facade.factory;

import br.com.brainyit.posystem2.facade.domain.CustomerFacade;


/**
 * @author rafael
 *
 */
public final class FacadeFactoryManager {

	private static FacadeFactoryManager instance;
	
	private CustomerFacadeFactory customerFacadeFactory;
	
	private FacadeFactoryManager(){}
	
	/**
	 * 
	 * @return
	 */
	public static FacadeFactoryManager getInstance() {
		if (instance==null) {
			instance = new FacadeFactoryManager();
		}
		return instance;
	}
	/**
	 * Cria uma instancia da factory de {@link CustomerFacade}
	 * 
	 * @return
	 */
	public CustomerFacadeFactory getCustomerFacadeFactory() {
		if (this.customerFacadeFactory==null) {
			this.customerFacadeFactory = new CustomerFacadeFactory();
		}
		return this.customerFacadeFactory;
	}
	
	
}
