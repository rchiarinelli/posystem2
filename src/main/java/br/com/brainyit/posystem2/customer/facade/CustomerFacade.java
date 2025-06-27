/**
 *
 */
package br.com.brainyit.posystem2.customer.facade;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.brainyit.posystem2.customer.mapper.IndividualMapper;
import br.com.brainyit.posystem2.customer.repository.CustomerRepository;
import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.CustomerStatus;
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

    public List<CustomerRestModel> list(final String subscriberUuid) {
        log.debug("Listing customers for subscriber {}", subscriberUuid);
        return customerRepository.findBySubscriberUuid(subscriberUuid).stream().map(this::map).toList();
    }

    public CustomerRestModel get(final String subscriberUuid, final Integer identity) {
        log.debug("Getting customer for subscriber {} and identity {}", subscriberUuid, identity);

        final List<Customer> result = customerRepository.findCustomersBySubscriberUuidAndCustomerId(subscriberUuid,
                identity);
        return result.stream().map(this::map).findFirst()
                .orElseThrow(() -> new BusinessException("Customer not found."));
    }

    CustomerRestModel map(final Customer customer) {
        CustomerRestModel restBean = new CompanyRestModel();
        restBean.setDetails(customer.getDetails());
        restBean.setId(customer.getId().toString());
        restBean.setName(customer.getName());
        restBean.setStatus(customer.getStatus().toString());
        return restBean;
    }

    @Transactional
    public IndividualRestModel createIndividual(final String subscriberUuid, final Individual individual) {
        log.debug("Creating individual customer for subscriber {}", subscriberUuid);

        if (individual == null) {
            throw new BusinessException("Individual cannot be null.");
        }

        if (individual.getName() == null || individual.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        final Subscriber subscriber = subscriberRepository.findByUuid(subscriberUuid)
                .orElseThrow(() -> new BusinessException("Subscriber not found."));

        individual.setSubscriber(subscriber);
        individual.setStatus(CustomerStatus.ACTIVE);
        return IndividualMapper.builder().build().mapFromDomain(customerRepository.save(individual));
    }

    @Transactional
    public IndividualRestModel updateIndividual(final String subscriberId, final Individual individual) {
        log.debug("Updating individual");

        if (individual == null) {
            throw new BusinessException("Individual cannot be null.");
        }

        if (individual.getName() == null || individual.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        List<Customer> existingCustomers = customerRepository.findCustomersBySubscriberUuidAndCustomerId(subscriberId,
                individual.getId());
        if (existingCustomers.isEmpty()) {
            throw new BusinessException("Customer not found.");
        }

        individual.setSubscriber(existingCustomers.get(0).getSubscriber());

        return IndividualMapper.builder().build().mapFromDomain(customerRepository.save(individual));
    }

    @Transactional
    public Company createCompany(final String subscriberUuid, final Company company) {
        log.debug("Creating company customer for subscriber {}", subscriberUuid);

        if (company == null) {
            throw new BusinessException("Company cannot be null.");
        }

        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        company.setSubscriber(subscriberRepository.findByUuid(subscriberUuid)
                .orElseThrow(() -> new BusinessException("Subscriber not found.")));
        company.setStatus(CustomerStatus.ACTIVE);
        return customerRepository.save(company);
    }

    @Transactional
    public Company updateCompany(final String subscriberId, final Company company) {
        log.debug("Updating company");

        if (company == null) {
            throw new BusinessException("Company cannot be null.");
        }

        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new BusinessException("Name is required.");
        }

        final List<Customer> existingCustomers = customerRepository
                .findCustomersBySubscriberUuidAndCustomerId(subscriberId, company.getId());
        if (existingCustomers.isEmpty()) {
            throw new BusinessException("Company not found.");
        }

        company.setSubscriber(existingCustomers.get(0).getSubscriber());

        return customerRepository.save(company);
    }

    @Transactional
    public void deleteCustomer(final String subscriberId, final Integer customerId) {
        log.debug("Deleting customer");

        final List<Customer> existingCustomers = customerRepository
                .findCustomersBySubscriberUuidAndCustomerId(subscriberId, customerId);
        if (existingCustomers.isEmpty()) {
            throw new BusinessException("Customer not found.");
        }
        customerRepository.delete(existingCustomers.get(0));
    }

}
