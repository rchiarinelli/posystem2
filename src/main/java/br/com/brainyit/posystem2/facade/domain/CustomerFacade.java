/**
 *
 */
package br.com.brainyit.posystem2.facade.domain;

import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.mapper.IndividualMapper;
import br.com.brainyit.posystem2.repository.CustomerRepository;
import br.com.brainyit.posystem2.restbean.customer.CustomerRestfulBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rafael
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerFacade {

    private final CustomerRepository customerRepository;

    /* (non-Javadoc)
     * @see br.com.brainyit.posystem2.facade.Facade#list()
     */
    public List<? extends CustomerRestfulBean> list(final Long subscriberId) {
        log.debug("Listing customers for subscriber {}", subscriberId);
        return customerRepository.findBySubscriberId(subscriberId).stream().map(this::map).toList();
    }

    /* (non-Javadoc)
     * @see br.com.brainyit.posystem2.facade.Facade#get(java.lang.Object)inscrMunicipal
     */
    public CustomerRestfulBean get(final Long subscriberId, final Integer identity) {
        log.debug("Getting customer for subscriber {} and identity {}", subscriberId, identity);
        
        final List<Customer> result = customerRepository.findCustomersBySubscriberIdAndCustomerId(subscriberId, identity);
        return result.stream().map(this::map).findFirst().orElseThrow(() -> new BusinessException("Customer not found."));
    }

    CustomerRestfulBean map(final Customer customer) {
        CustomerRestfulBean restBean = new CustomerRestfulBean();
        restBean.setDetails(customer.getDetails());
        restBean.setId(customer.getId().toString());
        restBean.setName(customer.getName());
        restBean.setStatus(customer.getStatus().toString());
        return restBean;
    }

    public CustomerRestfulBean createIndividual(final Long subscriberId, final Individual individual) {
        log.debug("Creating individual customer for subscriber {}", subscriberId);
        
        if (individual == null) {
            throw new BusinessException("Individual cannot be null.");
        }
        
        if (individual.getName() == null || individual.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        individual.setSubscriber(Subscriber.builder().id(subscriberId).build());
        return IndividualMapper.builder().build().mapFromDomain(customerRepository.save(individual));
    }

    public CustomerRestfulBean updateIndividual(final Long subscriberId, final Individual individual) {
        log.debug("Updating individual");

        if (individual == null) {
            throw new BusinessException("Individual cannot be null.");
        }

        if (individual.getName() == null || individual.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        List<Customer> existingCustomers = customerRepository.findCustomersBySubscriberIdAndCustomerId(subscriberId, individual.getId());
        if (existingCustomers.isEmpty()) {
            throw new BusinessException("Customer not found.");
        }

        individual.setSubscriber(existingCustomers.get(0).getSubscriber());

        return IndividualMapper.builder().build().mapFromDomain(customerRepository.save(individual));
    }


}
