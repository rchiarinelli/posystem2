/**
 *
 */
package br.com.brainyit.posystem2.facade.domain;

import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.repository.CustomerRepository;
import br.com.brainyit.posystem2.restbean.customer.CustomerRestfulBean;
import lombok.RequiredArgsConstructor;
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
public class CustomerFacade {

    private final CustomerRepository customerRepository;

    /* (non-Javadoc)
     * @see br.com.brainyit.posystem2.facade.Facade#list()
     */
    public List<? extends CustomerRestfulBean> list() {
        List<CustomerRestfulBean> list = new ArrayList<>();


        return list;
    }

    /* (non-Javadoc)
     * @see br.com.brainyit.posystem2.facade.Facade#get(java.lang.Object)inscrMunicipal
     */
    public CustomerRestfulBean get(Integer identity) {
        final Optional<Customer> result = customerRepository.findById(identity);
        return result.map(this::map).orElseThrow(() -> new BusinessException("Customer not found."));
    }

    CustomerRestfulBean map(final Customer customer) {
        CustomerRestfulBean restBean = new CustomerRestfulBean();
        restBean.setDetails(customer.getDetails());
        restBean.setId(customer.getId().toString());
        restBean.setName(customer.getName());
        restBean.setStatus(customer.getStatus().toString());
        return restBean;
    }


}
