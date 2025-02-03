package br.com.brainyit.posystem2.po.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.po.ProductionOrder;
import br.com.brainyit.posystem2.po.api.ProductionOrderRestBean;
import br.com.brainyit.posystem2.util.MessagesUtil;

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
        LocalDate estimatedDueDate = LocalDate.now().plusDays(7);
        LocalDate actualDeliveryDate = LocalDate.now().plusDays(6);
        
        ProductionOrder po = ProductionOrder.builder()
                .poNumber("PO123")
                .order(order)
                .description("Test Production Order")
                .qtd(10)
                .estimatedTime(2.5)
                .openDate(openDate)
                .estimatedDueDate(estimatedDueDate)
                .actualDeliveryDate(actualDeliveryDate)
                .status(POStatus.PLANNING)
                .build();

        // when
        ProductionOrderRestBean result = ProductionOrderMapper.builder().build().toRestBean(po);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getPoNumber(), is(po.getPoNumber()));
        assertThat(result.getDescription(), is(po.getDescription()));
        assertThat(result.getStatus(), is(po.getStatus().name()));
        assertThat(result.getEstimatedTime(), is(po.getEstimatedTime()));
        assertThat(result.getOpenDate(), is(po.getOpenDate().format(DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern")))));
        assertThat(result.getEstimatedDueDate(), is(po.getEstimatedDueDate().format(DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern")))));
        assertThat(result.getQuantity(), is(po.getQtd()));
        assertThat(result.getActualDeliveryDate(), is(po.getActualDeliveryDate().format(DateTimeFormatter.ofPattern(MessagesUtil.getInstance().getSystemProp("date_pattern")))));
        assertThat(result.getCustomerName(), is(customer.getName()));
    }

    @Test
    void test_toRestBean_productionOrderMappedToRestBean_withNullValues() {

        // when
        ProductionOrderRestBean result = ProductionOrderMapper.builder().build().toRestBean(ProductionOrder.builder().build());

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.getPoNumber(), is(nullValue()));
        assertThat(result.getDescription(), is(nullValue()));
        assertThat(result.getStatus(), is(nullValue()));
        assertThat(result.getEstimatedTime(), is(0.0));
        assertThat(result.getOpenDate(), is(nullValue()));
        assertThat(result.getEstimatedDueDate(), is(nullValue()));
        assertThat(result.getQuantity(), is(0));
        assertThat(result.getActualDeliveryDate(), is(nullValue()));
        assertThat(result.getCustomerName(), is(nullValue()));
    }
}

