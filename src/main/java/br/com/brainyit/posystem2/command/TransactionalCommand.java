/**
 * 
 */
package br.com.brainyit.posystem2.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;
import lombok.extern.slf4j.Slf4j;


/**
 * @author rafael
 *
 */
@Slf4j
public class TransactionalCommand extends CompositeCommand {
	
	private Session session;
	
	/**
	 * 
	 * @param session
	 */
	public TransactionalCommand(Session session, Command...commands) {
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
			log.debug("Opening transaction.");
			tx = this.session.beginTransaction();
			super.execute();
			tx.commit();
			log.debug("Transaction committed.");
		} catch (HibernateException he) {
			log.error("Error during transaction", he);
			tx.rollback();
			log.debug("Transaction rolled back.");
			throw new SystemException(he);
		} catch (BusinessException bu) {
			log.error("Error during transaction", bu);
			tx.rollback();
			log.debug("Transaction rolled back.");
			throw bu;
		}
	}
}
