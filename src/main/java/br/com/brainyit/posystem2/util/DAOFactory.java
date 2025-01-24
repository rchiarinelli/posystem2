/**
 * 
 */
package br.com.brainyit.posystem2.util;

import br.com.brainyit.posystem2.dao.OrderDAOImpl;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;

/**
 * Factory para gerar instancias de {@link GenericDAO}.
 * 
 * @author rafael
 *
 */
public final class DAOFactory {

	private static DAOFactory instance;
	
	private DAOFactory(){}
	
	/**
	 * 
	 * @return
	 */
	public static DAOFactory getInstance() {
		if (instance==null) {
			instance = new DAOFactory();
		}
		return instance;
	}
	/**
	 * 
	 * @return
	 */
	public OrderDAOImpl getOrderDAO() {
		OrderDAOImpl dao = new OrderDAOImpl();
		dao.setSessionFactory(HibernateUtils.getSessionFactory());
		return dao;
	}
	
}
