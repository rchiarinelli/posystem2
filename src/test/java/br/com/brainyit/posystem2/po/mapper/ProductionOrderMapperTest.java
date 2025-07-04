package br.com.brainyit.posystem2.po.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.model.ProductionOrderRestModel;
import br.com.brainyit.posystem2.po.ProductionOrder;

class ProductionOrderMapperTest {

        @Test
        void test_toRestBean_productionOrderMappedToRestBean() {
                // given
                Individual customer = Individual.builder()
                                .name("Test Customer")
                                .build();

                Order order = Order.builder()
                                .customer(customer)
                                .build();

                LocalDate openDate = LocalDate.now();
                LocalDate estimatedCompletionDate = LocalDate.now().plusDays(7);
                LocalDate actualCompletionDate = LocalDate.now().plusDays(6);

                ProductionOrder po = ProductionOrder.builder()
                                .poNumber("PO123")
                                .order(order)
                                .instructions("Test Production Order")
                                .qtd(10)
                                .estimatedTime(2.5)
                                .openDate(openDate)
                                .estimatedCompletionDate(estimatedCompletionDate)
                                .actualCompletionDate(actualCompletionDate)
                                .status(POStatus.PLANNING)
                                .build();

                // when

                ProductionOrderRestModel result = ProductionOrderMapper.builder().build().toRestBean(po);

                // then
                assertThat(result, is(notNullValue()));
                assertThat(result.getPoNumber(), is(po.getPoNumber()));
                assertThat(result.getInstructions(), is(po.getInstructions()));
                assertThat(result.getStatus(), is(po.getStatus().name()));
                assertThat(result.getEstimatedTime(), is(po.getEstimatedTime()));
                assertThat(result.getOpenDate(), is(po.getOpenDate()));
                assertThat(result.getEstimatedCompletionDate(), is(po.getEstimatedCompletionDate()));
                assertThat(result.getQuantity(), is(po.getQtd()));
                assertThat(result.getActualCompletionDate(), is(po.getActualCompletionDate()));
                assertThat(result.getCustomerName(), is(customer.getName()));
                assertThat(result.getOrderCode(), is(order.getOrderCode()));
        }

        @Test
        void test_toRestBean_productionOrderMappedToRestBean_withNullValues() {

                // when
                ProductionOrderRestModel result = ProductionOrderMapper.builder().build()
                                .toRestBean(ProductionOrder.builder().order(Order.builder().orderCode("ORDER123")
                                                .build()).build());

                // then
                assertThat(result, is(notNullValue()));
                assertThat(result.getPoNumber(), is(nullValue()));
                assertThat(result.getInstructions(), is(nullValue()));
                assertThat(result.getStatus(), is(nullValue()));
                assertThat(result.getEstimatedTime(), is(0.0));
                assertThat(result.getOpenDate(), is(nullValue()));
                assertThat(result.getEstimatedCompletionDate(), is(nullValue()));
                assertThat(result.getQuantity(), is(0));
                assertThat(result.getActualCompletionDate(), is(nullValue()));
                assertThat(result.getCustomerName(), is(nullValue()));
        }
}
