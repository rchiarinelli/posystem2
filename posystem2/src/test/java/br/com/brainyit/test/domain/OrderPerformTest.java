package br.com.brainyit.test.domain;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Test;

import br.com.brainyit.posystem2.dao.OrderDAOImpl;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.test.domain.base.BaseDataStoreJUnitTest;

public class OrderPerformTest extends BaseDataStoreJUnitTest {

	@Test
	public void testPerform_Success() throws Exception {
		OrderDAOImpl dao = mock(OrderDAOImpl.class);
		
		Order order = new Order(dao);
		order.setOpenDate(new Date());
		order.setDeliverDate(new Date());
		order.setOrderCode("XXX" + System.currentTimeMillis());
		order.setPrice(100.00D);
		order.setQuantity(12);
		order.setStatus(OrderStatus.OPEN);
		order.setDescription("ABALBAALBALABA");

		when(dao.save(order)).thenReturn(true);
		Long id = order.perform(order);
		assertEquals(order.getId(), id);
	}
	
	@Test
	public void testPerform_Fail() throws Exception {
		OrderDAOImpl dao = mock(OrderDAOImpl.class);
		
		Order order = new Order(dao);
		order.setOpenDate(new Date());
		order.setDeliverDate(new Date());
		order.setOrderCode("XXX" + System.currentTimeMillis());
		order.setPrice(100.00D);
		order.setQuantity(12);
		order.setStatus(OrderStatus.OPEN);
		order.setDescription("ABALBAALBALABA");
 
		when(dao.save(order)).thenReturn(false);

		try {
			order.perform(order);
			fail("It should throw a BusinessException");
		} catch (BusinessException bu) {
			assertEquals("Failing saving order at datastore.", bu.getMessage());
		}
	}
}
