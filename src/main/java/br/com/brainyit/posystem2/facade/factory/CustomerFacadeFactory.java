/**
 * 
 */
package br.com.brainyit.posystem2.facade.factory;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.brainyit.posystem2.dao.CustomDAO;
import br.com.brainyit.posystem2.dao.CustomerDAOImpl;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.facade.domain.CustomerFacade;
import br.com.brainyit.posystem2.facade.filter.CustomerFilterFacade;
import br.com.brainyit.posystem2.util.HibernateUtils;

/**
 * @author rafael
 *
 */
public class CustomerFacadeFactory {
	
	private static Logger logger = Logger.getLogger(CustomerFacadeFactory.class);
	
	private CustomerFacade customerFacade;
	
	private CustomerFilterFacade customerFilterFacade;
	
	/**
	 * 
	 * @return
	 */
	public CustomerFacade createFacade() {
		Session session = HibernateUtils.getSessionFactory().getCurrentSession();
		CustomerDAOImpl customerDAOImpl = new CustomerDAOImpl();
		customerDAOImpl.setSession(session);
		logger.debug("Building CustomerFacade.");
		this.customerFacade = new CustomerFacade(customerDAOImpl);
		return this.customerFacade;
	}
	
	public CustomerFilterFacade createFilterFacade() {
		Session session = HibernateUtils.getSessionFactory().getCurrentSession();
		CustomDAO<Customer> customDAO = new CustomDAO<Customer>();
		customDAO.setSession(session);
		logger.debug("Building CustomerFilterFacade.");
		this.customerFilterFacade = new CustomerFilterFacade(customDAO);
		return this.customerFilterFacade;
	}
	
}
