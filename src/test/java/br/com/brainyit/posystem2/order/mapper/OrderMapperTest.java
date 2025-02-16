package br.com.brainyit.posystem2.order.mapper;



import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.order.api.OrderRestBean;

class OrderMapperTest {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    void test_toRestBean_orderMappedToRestBean() {
        // given
        Date deliverDate = new Date();
        Individual customer = Individual.builder()
                .name("Test Customer")
                .build();
                
        Order order = Order.builder()
                .orderCode("TEST123")
                .customer(customer)
                .description("Test Order")
                .price(100.0)
                .quantity(2.0)
                .deliverDate(deliverDate)
                .status(OrderStatus.PENDING)
                .build();

        // when
        OrderRestBean result = OrderMapper.builder().build().toRestBean(order);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getOrderCode(), is(order.getOrderCode()));
        assertThat(result.getCustomerName(), is(customer.getName()));
        assertThat(result.getOrderDescription(), is(order.getDescription()));
        assertThat(result.getPrice(), is(order.getPrice()));
        assertThat(result.getQuantity(), is(order.getQuantity()));
        assertThat(result.getDeliverDate(), is(dateFormat.format(deliverDate)));
        assertThat(result.getStatus(), is(order.getStatus().name()));
    }

    @Test
    void test_toRestBean_orderMappedToRestBean_withNullDeliverDate() {
        // given
        Order order = Order.builder()
                .orderCode("TEST123")
                .description("Test Order")
                .build();

        // when
        OrderRestBean result = OrderMapper.builder().build().toRestBean(order);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getOrderCode(), is(order.getOrderCode()));
        assertThat(result.getOrderDescription(), is(order.getDescription()));
        assertThat(result.getDeliverDate(), is(nullValue()));
    }

}

