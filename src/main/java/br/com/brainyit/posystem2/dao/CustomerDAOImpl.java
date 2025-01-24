package br.com.brainyit.posystem2.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.posystem2.domain.Customer;
/**
 * 
 * @author rafael
 *
 */
public class CustomerDAOImpl extends CustomDAO<Customer> {

	private Session session;
	
	@Override
	protected Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
		super.setSessionFactory(session.getSessionFactory());
	}
	@Override
	public List<? extends Customer> executeFilter(Filter filter) {
		return super.executeFilter(filter);
	}
}
