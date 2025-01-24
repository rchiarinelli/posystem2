/**
 * 
 */
package br.com.brainyit.posystem2.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.filterengine.HqlFilterEngine;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

/**
 * @author rafael
 *
 */
public class CustomDAO<D> extends GenericDAOImpl<D, Serializable> implements DAO<D> {
	
	private Session session;
	
	@Override
	protected Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
		super.setSessionFactory(session.getSessionFactory());
	}

	
	/*
	 * (non-Javadoc)
	 * @see br.com.brainyit.posystem2.dao.DAO#executeFilter(br.com.brainyit.filterengine.Filter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends D> executeFilter(Filter filter) {
		Transaction tx = this.getSession().beginTransaction();
		HqlFilterEngine filterEngine = new HqlFilterEngine(this.getSession());
		Query query = filterEngine.createQuery(filter);
		List<? extends D> results = query.list();
		tx.commit();
		return results;
	}
}
