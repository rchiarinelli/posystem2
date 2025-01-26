/**
 * 
 */
package br.com.brainyit.posystem2.facade.filter;

import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.facade.FilterProperties;
import br.com.brainyit.posystem2.customer.repository.CustomerRepository;
import br.com.brainyit.posystem2.customer.api.CustomerGridRownRestfulBean;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Filter facade para busca dos dados dos clientes.
 * 
 * @author rafael
 *
 */
@Component
@AllArgsConstructor
public class CustomerFilterFacade  {
	
	private CustomerRepository customerRepository;

	/*
	 * (non-Javadoc)
	 * @see br.com.brainyit.posystem2.facade.FilterFacade#filterData(java.lang.String)
	 */
	public List<? extends CustomerGridRownRestfulBean> filterData(Integer subscriber, List<FilterProperties> valuesMap, String sortField) {
		List<? extends Customer> customers = customerRepository.findAll();
		List<CustomerGridRownRestfulBean> restCustomerList = new ArrayList<>();
		CustomerGridRownRestfulBean bean = null;
		for (Customer customer : customers) {
			bean = new CustomerGridRownRestfulBean();
			bean.setName(customer.getName());
			bean.setCust_id(customer.getId());
			if (customer instanceof Individual) {
				bean.setCustIdent(((Individual)customer).getCpf());
				bean.setType("PF");
				bean.setTypeName("Pessoa física");
			}
			if (customer instanceof Company) {
				bean.setCustIdent(((Company)customer).getCnpjCgc());
				bean.setType("PJ");
				bean.setTypeName("Pessoa juridica");
			}
			restCustomerList.add(bean);
		}
		return restCustomerList;
	}
}
