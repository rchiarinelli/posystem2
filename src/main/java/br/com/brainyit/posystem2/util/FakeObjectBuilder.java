package br.com.brainyit.posystem2.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.PFCustomer;

public class FakeObjectBuilder {
	
	private FakeObjectBuilder(){ }
	
	/**
	 * 
	 * @return
	 */
	public static List<Order> buildOrderList() {
		List<Order> orderList = new ArrayList<Order>();
		
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "XPTO", new Date()));
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "rtyrty", new Date()));
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "dfgsdfg", new Date()));
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "dfg", new Date()));
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "dfgd", new Date()));
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "fgsfg", new Date()));
		orderList.add(new Order(10L, new PFCustomer(), new Date(), new Date(), String.valueOf(System.currentTimeMillis()), 103D, 10, OrderStatus.OPEN, "dfg", new Date()));
		
		return orderList;
	}

}
