/**
 * 
 */
package br.com.brainyit.posystem2.command.impl;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import br.com.brainyit.posystem2.command.Command;
import br.com.brainyit.posystem2.dao.OrderDAOImpl;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;

/**
 * @author rafael
 *
 */
public class PerformOrderCommand implements Command {
	
	private static Logger logger = Logger.getLogger(PerformOrderCommand.class);
	
	private Order order;
	
	private OrderDAOImpl dao;
	
	private Session session;
	
	/**
	 * @param order
	 * @param dao
	 */
	public PerformOrderCommand(Order order, OrderDAOImpl dao,Session session) {
		super();
		this.order = order;
		this.dao = dao;
		this.session = session;
	}



	/* (non-Javadoc)
	 * @see br.com.brainyit.posystem2.command.Command#execute()
	 */
	@Override
	public void execute() throws BusinessException, SystemException {
		logger.debug("Executing order perform");
		this.dao.setSession(this.session);
		logger.debug("Validating perform order businesse rules");
		//TODO implement business rules
		logger.debug("Saving Order");
		boolean status = this.dao.save(this.order);
		if (!status) {
			throw new BusinessException("Failing saving order at datastore.");
		}
	}
}
