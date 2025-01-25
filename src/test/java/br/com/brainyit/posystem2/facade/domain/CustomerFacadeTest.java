package br.com.brainyit.posystem2.facade.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.CustomerRepository;
import br.com.brainyit.posystem2.restbean.customer.CustomerRestfulBean;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerFacade customerFacade;

    private Customer mockCustomer;
    private static final Integer SUBSCRIBER_ID = 1;
    private static final Integer CUSTOMER_ID = 5;

    @BeforeEach
    void setUp() {
        mockCustomer = new Customer(CUSTOMER_ID, "Test Customer") {
            @Override
            public String getDetails() {
                return "Test Details";
            }
            @Override
            public CustomerStatus getStatus() {
                return CustomerStatus.ACTIVE;
            }
        };
    }

    @Test
    void test_list_whenCustomersExist_returnsCustomerList() {
        // Arrange
        when(customerRepository.findBySubscriberId(SUBSCRIBER_ID))
                .thenReturn(List.of(mockCustomer));

        // Act
        List<? extends CustomerRestfulBean> result = customerFacade.list(SUBSCRIBER_ID);

        // Assert
        assertThat(result, is(not(empty())));
        assertThat(result.size(), is(1));
        CustomerRestfulBean resultBean = result.get(0);
        assertThat(resultBean.getId(), is(CUSTOMER_ID.toString()));
        assertThat(resultBean.getName(), is("Test Customer"));
        assertThat(resultBean.getDetails(), is("Test Details"));
        assertThat(resultBean.getStatus(), is(CustomerStatus.ACTIVE.toString()));
    }

    @Test
    void test_list_whenNoCustomersExist_returnsEmptyList() {
        // Arrange
        when(customerRepository.findBySubscriberId(SUBSCRIBER_ID))
                .thenReturn(List.of());

        // Act
        List<? extends CustomerRestfulBean> result = customerFacade.list(SUBSCRIBER_ID);

        // Assert
        assertThat(result, is(empty()));
    }

    @Test
    void test_get_whenCustomerExists_returnsCustomer() {
        // Arrange
        when(customerRepository.findCustomersBySubscriberIdAndCustomerId(SUBSCRIBER_ID, CUSTOMER_ID))
                .thenReturn(List.of(mockCustomer));

        // Act
        CustomerRestfulBean result = customerFacade.get(SUBSCRIBER_ID, CUSTOMER_ID);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(CUSTOMER_ID.toString()));
        assertThat(result.getName(), is("Test Customer"));
        assertThat(result.getDetails(), is("Test Details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
    }

    @Test
    void test_get_whenCustomerDoesNotExist_throwsBusinessException() {
        // Arrange
        when(customerRepository.findCustomersBySubscriberIdAndCustomerId(any(), any()))
                .thenReturn(List.of());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.get(SUBSCRIBER_ID, CUSTOMER_ID));
        assertThat(exception.getMessage(), is("Customer not found."));
    }

    @Test
    void test_map_convertsCustomerToRestBean() {
        // Act
        CustomerRestfulBean result = customerFacade.map(mockCustomer);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(CUSTOMER_ID.toString()));
        assertThat(result.getName(), is("Test Customer"));
        assertThat(result.getDetails(), is("Test Details"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
    }
}
