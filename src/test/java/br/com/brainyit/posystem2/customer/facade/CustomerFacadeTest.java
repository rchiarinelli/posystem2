package br.com.brainyit.posystem2.customer.facade;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import br.com.brainyit.posystem2.customer.api.CustomerRestfulBean;
import br.com.brainyit.posystem2.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {

    @InjectMocks
    private CustomerFacade customerFacade;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SubscriberRepository subscriberRepository;

    private Customer mockCustomer;
    
    private static final Long SUBSCRIBER_ID = 1L;
    
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

    @Test
    void test_createIndividual_whenValidInput_createsAndReturnsCustomer() {
        // Arrange
        Individual customer = new Individual();
        customer.setName("John Doe");
        customer.setDetails("New customer");
        customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCpf("1234567890");
        customer.setRg("1234567890");
        customer.setSubscriber(Subscriber.builder().id(SUBSCRIBER_ID).build());


        Individual savedCustomer = new Individual();
        savedCustomer.setName("John Doe");
        savedCustomer.setDetails("New customer");
        savedCustomer.setId(CUSTOMER_ID);
        savedCustomer.setStatus(CustomerStatus.ACTIVE);
        savedCustomer.setCpf("1234567890");
        savedCustomer.setRg("1234567890");
        savedCustomer.setSubscriber(Subscriber.builder().id(SUBSCRIBER_ID).build());

        when(customerRepository.save(customer)).thenReturn(savedCustomer);

        // Act
        CustomerRestfulBean result = customerFacade.createIndividual(SUBSCRIBER_ID, customer);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(CUSTOMER_ID.toString()));
        assertThat(result.getName(), is("John Doe"));
        assertThat(result.getDetails(), is("New customer"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE.toString()));
    }

    @Test
    void test_createIndividual_whenNullInput_throwsBusinessException() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.createIndividual(SUBSCRIBER_ID, null));
        assertThat(exception.getMessage(), is("Individual cannot be null."));
    }

    @Test
    void test_createIndividual_whenInvalidInput_throwsBusinessException() {
        // Arrange
        Individual individual = new Individual();
        // Missing required name

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.createIndividual(SUBSCRIBER_ID, individual));
        assertThat(exception.getMessage(), is("Name is required."));
    }

    @Test
    void test_updateIndividual_whenValidInput_updatesAndReturnsCustomer() {
        // Arrange
        Individual existingCustomer = new Individual();
        existingCustomer.setId(CUSTOMER_ID);
        existingCustomer.setName("John Doe");
        existingCustomer.setDetails("Old details");
        existingCustomer.setStatus(CustomerStatus.ACTIVE);
        existingCustomer.setCpf("1234567890");
        existingCustomer.setRg("1234567890");
        existingCustomer.setSubscriber(Subscriber.builder().id(SUBSCRIBER_ID).build());

        Individual updatedCustomer = new Individual();
        updatedCustomer.setId(CUSTOMER_ID);
        updatedCustomer.setName("John Smith");
        updatedCustomer.setDetails("Updated details");
        updatedCustomer.setStatus(CustomerStatus.INACTIVE);
        updatedCustomer.setCpf("9876543210");
        updatedCustomer.setRg("9876543210");
        updatedCustomer.setSubscriber(Subscriber.builder().id(SUBSCRIBER_ID).build());

        when(customerRepository.findCustomersBySubscriberIdAndCustomerId(SUBSCRIBER_ID, CUSTOMER_ID))
            .thenReturn(List.of(existingCustomer));
        when(customerRepository.save(updatedCustomer)).thenReturn(updatedCustomer);

        // Act
        CustomerRestfulBean result = customerFacade.updateIndividual(SUBSCRIBER_ID, updatedCustomer);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is("John Smith"));
        assertThat(result.getDetails(), is("Updated details")); 
        assertThat(result.getStatus(), is(CustomerStatus.INACTIVE.toString()));
    }

    @Test
    void test_updateIndividual_whenCustomerNotFound_throwsBusinessException() {
        // Arrange
        Individual updatedCustomer = new Individual();
        updatedCustomer.setId(CUSTOMER_ID);
        updatedCustomer.setName("John Smith");
        
        when(customerRepository.findCustomersBySubscriberIdAndCustomerId(SUBSCRIBER_ID, CUSTOMER_ID))
            .thenReturn(List.of());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.updateIndividual(SUBSCRIBER_ID, updatedCustomer));
        assertThat(exception.getMessage(), is("Customer not found."));
    }

    @Test
    void test_updateIndividual_whenNullInput_throwsBusinessException() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.updateIndividual(SUBSCRIBER_ID, null));
        assertThat(exception.getMessage(), is("Individual cannot be null."));
    }

    @Test
    void test_updateIndividual_whenInvalidInput_throwsBusinessException() {
        // Arrange
        Individual individual = new Individual();
        // Missing required name

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.updateIndividual(SUBSCRIBER_ID, individual));
        assertThat(exception.getMessage(), is("Name is required."));
    }

    @Test
    void test_createCompany_whenValidInput_returnsCompany() {

        Subscriber subscriber = Subscriber.builder().id(SUBSCRIBER_ID).build();

        // Arrange
        Company company = new Company();
        company.setName("ACME Corporation");
        company.setDetails("Large company");
        company.setStatus(CustomerStatus.ACTIVE);
        company.setCnpjCgc("12.345.678/0001-90");
        company.setInscrEstadual("123456789");
        company.setInscrMunicipal("987654321");
        
        Company savedCompany = new Company();
        savedCompany.setId(CUSTOMER_ID);
        savedCompany.setName("ACME Corporation");
        savedCompany.setDetails("Large company");
        savedCompany.setStatus(CustomerStatus.ACTIVE);
        savedCompany.setCnpjCgc("12.345.678/0001-90");
        savedCompany.setInscrEstadual("123456789");
        savedCompany.setInscrMunicipal("987654321");
        savedCompany.setSubscriber(subscriber);
        
        when(subscriberRepository.findById(SUBSCRIBER_ID)).thenReturn(Optional.of(subscriber));
        when(customerRepository.save(company)).thenReturn(savedCompany);

        // Act
        Company result = customerFacade.createCompany(SUBSCRIBER_ID, company);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(CUSTOMER_ID));
        assertThat(result.getName(), is("ACME Corporation"));
        assertThat(result.getDetails(), is("Large company"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE));
        assertThat(result.getCnpjCgc(), is("12.345.678/0001-90"));
        assertThat(result.getInscrEstadual(), is("123456789"));
        assertThat(result.getInscrMunicipal(), is("987654321"));
        assertThat(result.getSubscriber(), is(subscriber));
    }

    @Test
    void test_createCompany_whenNullInput_throwsBusinessException() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.createCompany(SUBSCRIBER_ID, null));
        assertThat(exception.getMessage(), is("Company cannot be null."));
    }

    @Test
    void test_createCompany_whenInvalidInput_throwsBusinessException() {
        // Arrange
        Company company = new Company();
        // Missing required name

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.createCompany(SUBSCRIBER_ID, company));
        assertThat(exception.getMessage(), is("Name is required."));
    }


    @Test
    void test_createCompany_whenSubscriberNotFound_throwsBusinessException() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.createCompany(SUBSCRIBER_ID, Company.builder().name("ACME Corporation").build()));
        assertThat(exception.getMessage(), is("Subscriber not found."));
    }

    @Test
    void test_updateCompany_whenValidInput_updatesAndReturnsCompany() {
        // Arrange
        Company company = new Company();
        company.setId(CUSTOMER_ID);
        company.setName("ACME Corporation");
        company.setDetails("Updated company");
        company.setStatus(CustomerStatus.ACTIVE);
        company.setCnpjCgc("12.345.678/0001-90");
        company.setInscrEstadual("123456789");
        company.setInscrMunicipal("987654321");

        Subscriber subscriber = Subscriber.builder()
                .id(SUBSCRIBER_ID)
                .build();

        Company savedCompany = new Company();
        savedCompany.setId(CUSTOMER_ID);
        savedCompany.setName("ACME Corporation");
        savedCompany.setDetails("Updated company");
        savedCompany.setStatus(CustomerStatus.ACTIVE);
        savedCompany.setCnpjCgc("12.345.678/0001-90");
        savedCompany.setInscrEstadual("123456789");
        savedCompany.setInscrMunicipal("987654321");
        savedCompany.setSubscriber(subscriber);

        when(customerRepository.findCustomersBySubscriberIdAndCustomerId(SUBSCRIBER_ID, CUSTOMER_ID))
                .thenReturn(List.of(Company.builder().build()));
        when(customerRepository.save(company)).thenReturn(savedCompany);

        // Act
        Company result = customerFacade.updateCompany(SUBSCRIBER_ID, company);

        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result.getId(), is(CUSTOMER_ID));
        assertThat(result.getName(), is("ACME Corporation"));
        assertThat(result.getDetails(), is("Updated company"));
        assertThat(result.getStatus(), is(CustomerStatus.ACTIVE));
        assertThat(result.getCnpjCgc(), is("12.345.678/0001-90"));
        assertThat(result.getInscrEstadual(), is("123456789"));
        assertThat(result.getInscrMunicipal(), is("987654321"));
        assertThat(result.getSubscriber(), is(subscriber));
    }

    @Test
    void test_updateCompany_whenCompanyNotFound_throwsBusinessException() {
        // Arrange
        Company company = new Company();
        company.setId(CUSTOMER_ID);
        company.setName("ACME Corporation");

        when(customerRepository.findCustomersBySubscriberIdAndCustomerId(SUBSCRIBER_ID, CUSTOMER_ID))
                .thenReturn(List.of());

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.updateCompany(SUBSCRIBER_ID, company));
        assertThat(exception.getMessage(), is("Company not found."));
    }

    @Test
    void test_updateCompany_whenNullInput_throwsBusinessException() {
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.updateCompany(SUBSCRIBER_ID, null));
        assertThat(exception.getMessage(), is("Company cannot be null."));
    }

    @Test
    void test_updateCompany_whenInvalidInput_throwsBusinessException() {
        // Arrange
        Company company = new Company();
        company.setId(CUSTOMER_ID);
        // Missing required name

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerFacade.updateCompany(SUBSCRIBER_ID, company));
        assertThat(exception.getMessage(), is("Name is required."));
    }

}
