/**
 * 
 */
package br.com.brainyit.posystem2.facade.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Transaction;

import br.com.brainyit.posystem2.dao.CustomerDAOImpl;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.facade.DomainFacade;
import br.com.brainyit.posystem2.restbean.customer.CustomerRestfulBean;
import br.com.brainyit.posystem2.util.HibernateUtils;

/**
 * @author rafael
 *
 */
public class CustomerFacade implements DomainFacade<CustomerRestfulBean,Customer>{

	private CustomerDAOImpl customerDAOImpl;
	
	/**
	 * @param customerDAOImpl
	 */
	public CustomerFacade(CustomerDAOImpl customerDAOImpl) {
		super();
		this.customerDAOImpl = customerDAOImpl;
	}

	/* (non-Javadoc)
	 * @see br.com.brainyit.posystem2.facade.Facade#list()
	 */
	public List<? extends CustomerRestfulBean> list() {
		List<CustomerRestfulBean> list = new ArrayList<CustomerRestfulBean>();
//		list.add(new PFCustomer(1,"João da Silva","5675674567",123123L));
//		list.add(new PFCustomer(2,"João dos Santos","325312",908908L));
//		list.add(new PFCustomer(3,"Maria da Silva","452345",56737567L));
//		list.add(new PJCustomer(4, "BrayniIT", "BGBGBBGBGBG", 998989L, 8989898L));
//		list.add(new PJCustomer(5, "Rian Usinagem", "LABALLABLA", 998989L, 8989898L));
//		list.add(new PJCustomer(6, "Xing Ling Usinagem", "UOULULUL", 998989L, 8989898L));
		
		return list;
	}

	/* (non-Javadoc)
	 * @see br.com.brainyit.posystem2.facade.Facade#get(java.lang.Object)inscrMunicipal
	 */
	public CustomerRestfulBean get(Serializable identity) {
		Transaction tx = HibernateUtils.getSessionFactory().getCurrentSession().beginTransaction();
		Customer customer = this.customerDAOImpl.find(identity);
		tx.commit();
		CustomerRestfulBean restBean = null;
		if (customer!=null) {
			restBean= new CustomerRestfulBean();
			restBean.setDetails(customer.getDetails());
			restBean.setId(customer.getId().toString());
			restBean.setName(customer.getName());
			restBean.setStatus(customer.getStatus().toString());
		}
		return restBean;
	}

}
