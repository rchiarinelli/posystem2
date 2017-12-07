/**
 * 
 */
package br.com.brainyit.posystem2.facade.filter;

import java.util.ArrayList;
import java.util.List;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.posystem2.dao.CustomDAO;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.PFCustomer;
import br.com.brainyit.posystem2.domain.PJCustomer;
import br.com.brainyit.posystem2.facade.AbstractFilterFacade;
import br.com.brainyit.posystem2.facade.FilterProperties;
import br.com.brainyit.posystem2.restbean.customer.CustomerGridRownRestfulBean;


/**
 * Filter facade para busca dos dados dos clientes.
 * 
 * @author rafael
 *
 */
public class CustomerFilterFacade extends AbstractFilterFacade<CustomerGridRownRestfulBean,Customer> {
	
	private CustomDAO<Customer> dao;
	
	/**
	 * @param customerDAOImpl
	 */
	public CustomerFilterFacade(CustomDAO<Customer> dao) {
		super();
		this.dao = dao;
	}
	/*
	 * (non-Javadoc)
	 * @see br.com.brainyit.posystem2.facade.FilterFacade#filterData(java.lang.String)
	 */
	@Override
	public List<? extends CustomerGridRownRestfulBean> filterData(Integer subscriber, List<FilterProperties> valuesMap, String sortField) {
		Filter filter = this.buildFilter(subscriber, valuesMap, sortField);
		List<? extends Customer> customers = this.dao.executeFilter(filter);
		List<CustomerGridRownRestfulBean> restCustomerList = new ArrayList<CustomerGridRownRestfulBean>();		
		CustomerGridRownRestfulBean bean = null;
		for (Customer customer : customers) {
			bean = new CustomerGridRownRestfulBean();
			bean.setName(customer.getName());
			bean.setCust_id(customer.getId());
			if (customer instanceof PFCustomer) {
				bean.setCustIdent(((PFCustomer)customer).getCpf());
				bean.setType("PF");
				bean.setTypeName("Pessoa física");
			}
			if (customer instanceof PJCustomer) {
				bean.setCustIdent(((PJCustomer)customer).getCnpjCgc());
				bean.setType("PJ");
				bean.setTypeName("Pessoa juridica");
			}
			restCustomerList.add(bean);
		}
		return restCustomerList;
	}
}
