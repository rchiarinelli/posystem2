/**
 *
 */
package br.com.brainyit.posystem2.customer.facade;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.brainyit.posystem2.customer.mapper.IndividualMapper;
import br.com.brainyit.posystem2.customer.repository.CustomerRepository;
import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.model.CompanyRestModel;
import br.com.brainyit.posystem2.model.CustomerRestModel;
import br.com.brainyit.posystem2.model.IndividualRestModel;
import br.com.brainyit.posystem2.repository.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rafael
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerFacade {

    private final CustomerRepository customerRepository;

    private final SubscriberRepository subscriberRepository;

    /* (non-Javadoc)
     * @see br.com.brainyit.posystem2.facade.Facade#list()
     */
    public List<CustomerRestModel> list(final Long subscriberId) {
        log.debug("Listing customers for subscriber {}", subscriberId);
        return customerRepository.findBySubscriberId(subscriberId).stream().map(this::map).toList();
    }

    /* (non-Javadoc)
     * @see br.com.brainyit.posystem2.facade.Facade#get(java.lang.Object)inscrMunicipal
     */
    public CustomerRestModel get(final Long subscriberId, final Integer identity) {
        log.debug("Getting customer for subscriber {} and identity {}", subscriberId, identity);
        
        final List<Customer> result = customerRepository.findCustomersBySubscriberIdAndCustomerId(subscriberId, identity);
        return result.stream().map(this::map).findFirst().orElseThrow(() -> new BusinessException("Customer not found."));
    }

    CustomerRestModel map(final Customer customer) {
        CustomerRestModel restBean = new CompanyRestModel();
        restBean.setDetails(customer.getDetails());
        restBean.setId(customer.getId().toString());
        restBean.setName(customer.getName());
        restBean.setStatus(customer.getStatus().toString());
        return restBean;
    }

    public IndividualRestModel createIndividual(final Long subscriberId, final Individual individual) {
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

    public IndividualRestModel updateIndividual(final Long subscriberId, final Individual individual) {
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

    public Company createCompany(final Long subscriberId, final Company company) {
        log.debug("Creating company customer for subscriber {}", subscriberId);
        
        if (company == null) {
            throw new BusinessException("Company cannot be null.");
        }
        
        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        company.setSubscriber(subscriberRepository.findById(subscriberId).orElseThrow(() -> new BusinessException("Subscriber not found.")));
        return customerRepository.save(company);
    }

    public Company updateCompany(final Long subscriberId, final Company company) {
        log.debug("Updating company");

        if (company == null) {
            throw new BusinessException("Company cannot be null.");
        }

        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        final List<Customer> existingCustomers = customerRepository.findCustomersBySubscriberIdAndCustomerId(subscriberId, company.getId());
        if (existingCustomers.isEmpty()) {
            throw new BusinessException("Company not found.");
        }

        company.setSubscriber(existingCustomers.get(0).getSubscriber());

        return customerRepository.save(company);
    }


}
