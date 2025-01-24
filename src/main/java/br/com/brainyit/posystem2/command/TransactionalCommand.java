/**
 * 
 */
package br.com.brainyit.posystem2.command;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;


/**
 * @author rafael
 *
 */
public class TransactionalCommand extends CompositeCommand {
	
	private static Logger logger = Logger.getLogger(TransactionalCommand.class);
	
	private Session session;
	
	/**
	 * 
	 * @param session
	 */
	public TransactionalCommand(Session session,Command...commands) {
		super(commands);
		this.session = session;
	}

	/* (non-Javadoc)
	 * @see br.com.brainyit.posystem2.command.Command#execute(br.com.brainyit.posystem2.command.Command)
	 */
	@Override
	public void execute() throws BusinessException, SystemException {
		Transaction tx = null;
		try {
			logger.debug("Opening transaction.");
			tx = this.session.beginTransaction();
			super.execute();
			tx.commit();
			logger.debug("Transaction committed.");
		} catch (HibernateException he) {
			logger.error(he);
			tx.rollback();
			logger.debug("Transaction rolled back.");
			throw new SystemException(he);
		} catch (BusinessException bu) {
			logger.error(bu);
			tx.rollback();
			logger.debug("Transaction rolled back.");
			throw bu;
		}
	}
}
