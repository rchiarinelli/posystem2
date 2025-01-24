package br.com.brainyit.posystem2.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import org.apache.commons.lang3.RandomStringUtils;

public class FakeObjectBuilder {
	
	private FakeObjectBuilder(){ }
	
	/**
	 * 
	 * @return
	 */
	public static List<Order> buildOrderList() {
		List<Order> orders = new ArrayList<>();

		orders.add(Order.builder().id(1L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD1").deliverDate(new Date()).status(OrderStatus.OPEN).build());
		orders.add(Order.builder().id(10L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD2").deliverDate(new Date()).status(OrderStatus.OPEN).build());
		orders.add(Order.builder().id(2L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD3").deliverDate(new Date()).status(OrderStatus.OPEN).build());orders.add(Order.builder().id(10L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD").deliverDate(new Date()).status(OrderStatus.OPEN).build());
		orders.add(Order.builder().id(20L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD4").deliverDate(new Date()).status(OrderStatus.OPEN).build());
		orders.add(Order.builder().id(3L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD5").deliverDate(new Date()).status(OrderStatus.OPEN).build());
		orders.add(Order.builder().id(30L).customer(Individual.builder().build()).description(RandomStringUtils.randomAlphabetic(10)).price(10.3).quantity(10).orderCode("ABCD6").deliverDate(new Date()).status(OrderStatus.OPEN).build());

		return orders;
	}

}
