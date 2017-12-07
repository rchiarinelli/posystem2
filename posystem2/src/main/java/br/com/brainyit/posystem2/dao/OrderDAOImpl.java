/**
 * 
 */
package br.com.brainyit.posystem2.dao;

import org.hibernate.Session;

import br.com.brainyit.posystem2.domain.Order;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

/**
 * @author rafael
 * 
 */
public class OrderDAOImpl extends GenericDAOImpl<Order, Long> {
	private Session session;
	
	@Override
	protected Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
		super.setSessionFactory(session.getSessionFactory());
	}
}
