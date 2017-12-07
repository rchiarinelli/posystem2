/**
 * 
 */
package br.com.brainyit.posystem2.facade.filter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.brainyit.filterengine.FilterCondition;
import br.com.brainyit.posystem2.dao.CustomDAO;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.facade.FilterProperties;
import br.com.brainyit.posystem2.restbean.customer.CustomerGridRownRestfulBean;
import br.com.brainyit.posystem2.util.HibernateUtils;
import br.com.brainyit.test.domain.base.BaseDataStoreJUnitTest;

/**
 * @author rafael
 *
 */
public class CustomerFilterFacadeTest extends BaseDataStoreJUnitTest {

	@Test
	public void testFilterData_NullSortField() throws Exception {
		CustomDAO<Customer> dao = new CustomDAO<Customer>();
		dao.setSession(HibernateUtils.getSessionFactory().getCurrentSession());
		CustomerFilterFacade customerFilterFacade = new CustomerFilterFacade(dao);
		List<FilterProperties> filterArgs = new ArrayList<FilterProperties>();
		filterArgs.add(new FilterProperties("customerStatus",FilterCondition.EQUALS, CustomerStatus.ACTIVE));
		List<? extends CustomerGridRownRestfulBean> results =  customerFilterFacade.filterData(1, filterArgs, null);
		Assert.assertNotNull(results);
		Assert.assertEquals(6, results.size());
	}
	
	@Test
	public void testFilterData_NameSortField() throws Exception {
		CustomDAO<Customer> dao = new CustomDAO<Customer>();
		dao.setSession(HibernateUtils.getSessionFactory().getCurrentSession());
		CustomerFilterFacade customerFilterFacade = new CustomerFilterFacade(dao);
		List<FilterProperties> filterArgs = new ArrayList<FilterProperties>();
		filterArgs.add(new FilterProperties("customerStatus",FilterCondition.EQUALS, CustomerStatus.ACTIVE));
		List<? extends CustomerGridRownRestfulBean> results =  customerFilterFacade.filterData(1, filterArgs, "name");
		Assert.assertNotNull(results);
		Assert.assertEquals(6, results.size());
		//Verificar a ordem
		Assert.assertEquals("AAAAAA", results.get(0).getName());
		Assert.assertEquals("ABCDC Retifica", results.get(1).getName());
		Assert.assertEquals("CCCCC", results.get(2).getName());
		Assert.assertEquals("Usinagem XPTO", results.get(3).getName());
		Assert.assertEquals("WWWWWWW", results.get(4).getName());
		Assert.assertEquals("XYZ LABALBAAL", results.get(5).getName());
	}
	
	@Test
	public void testFilterData_FilterByName() throws Exception {
		CustomDAO<Customer> dao = new CustomDAO<Customer>();
		dao.setSession(HibernateUtils.getSessionFactory().getCurrentSession());
		CustomerFilterFacade customerFilterFacade = new CustomerFilterFacade(dao);
		List<FilterProperties> filterArgs = new ArrayList<FilterProperties>();
		filterArgs.add(new FilterProperties("customerStatus",FilterCondition.EQUALS, CustomerStatus.ACTIVE));
		filterArgs.add(new FilterProperties("name",FilterCondition.LIKE, "%a%"));
		List<? extends CustomerGridRownRestfulBean> results =  customerFilterFacade.filterData(1, filterArgs, "name");
		Assert.assertNotNull(results);
		Assert.assertEquals(2, results.size());
		//Verificar a ordem
		Assert.assertEquals("ABCDC Retifica", results.get(0).getName());
		Assert.assertEquals("Usinagem XPTO", results.get(1).getName());
	}
}
 