package br.com.brainyit.test.domain;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.OrderRepository;
import br.com.brainyit.test.domain.base.BaseDataStoreJUnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPerformTest extends BaseDataStoreJUnitTest {

    @InjectMocks
    private OrderRepository orderRepository;

    @Test
    void testPerform_Success() throws Exception {

        Order order = new Order();
        order.setOpenDate(new Date());
        order.setDeliverDate(new Date());
        order.setOrderCode("XXX" + System.currentTimeMillis());
        order.setPrice(100.00D);
        order.setQuantity(12);
        order.setStatus(OrderStatus.OPEN);
        order.setDescription("ABALBAALBALABA");

        when(orderRepository.saveAndFlush(order)).thenReturn(order);
        Long id = order.perform(order);
        assertEquals(order.getId(), id);
    }

    @Test
    void testPerform_Fail() throws Exception {

        Order order = new Order();
        order.setOpenDate(new Date());
        order.setDeliverDate(new Date());
        order.setOrderCode("XXX" + System.currentTimeMillis());
        order.setPrice(100.00D);
        order.setQuantity(12);
        order.setStatus(OrderStatus.OPEN);
        order.setDescription("ABALBAALBALABA");

        when(orderRepository.saveAndFlush(order)).thenReturn(null);

        try {
            order.perform(order);
            fail("It should throw a BusinessException");
        } catch (BusinessException bu) {
            assertEquals("Failing saving order at datastore.", bu.getMessage());
        }
    }
}
