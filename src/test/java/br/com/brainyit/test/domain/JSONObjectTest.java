package br.com.brainyit.test.domain;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.brainyit.posystem2.restbean.customer.CustomerGridRownRestfulBean;
import br.com.brainyit.posystem2.restful.util.JSONGridObject;
import br.com.brainyit.posystem2.restful.util.JSONViewHelper;

public class JSONObjectTest {

	@Test
	public void testGridJSON() throws Exception{
		List<CustomerGridRownRestfulBean> restCustomerLit = new ArrayList<CustomerGridRownRestfulBean>();
		
		restCustomerLit.add(new CustomerGridRownRestfulBean(1, "Maria da Silva", "34234", "Pessoa juridica", "PF"));
		restCustomerLit.add(new CustomerGridRownRestfulBean(1, "Maria da Silva", "34234", "Pessoa juridica", "PF"));
		restCustomerLit.add(new CustomerGridRownRestfulBean(1, "Maria da Silva", "34234", "Pessoa juridica", "PF"));
		restCustomerLit.add(new CustomerGridRownRestfulBean(1, "Maria da Silva", "34234", "Pessoa juridica", "PF"));
		
		JSONViewHelper viewHelper = new JSONViewHelper();
		JSONGridObject gridResponse = viewHelper.convertBeanListToJSONGridItems(restCustomerLit);
		System.out.println(gridResponse.toString());
	}
	
}
