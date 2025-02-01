/**
 *
 */
package br.com.brainyit.posystem2.customer.api;

import br.com.brainyit.posystem2.customer.facade.CustomerFacade;
import br.com.brainyit.posystem2.customer.mapper.CompanyMapper;
import br.com.brainyit.posystem2.customer.mapper.IndividualMapper;
import br.com.brainyit.posystem2.restbean.response.Message;
import br.com.brainyit.posystem2.util.MessagesUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author rafael
 */
@RestController
@RequestMapping("/{subscriber}/customers")
@RequiredArgsConstructor
public class CustomerEndpoint {

    private static final Logger logger = LogManager.getLogger(CustomerEndpoint.class);

    private final CustomerFacade customerFacade;


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getIndividual(@PathVariable("subscriber") final String subscriber, @PathVariable("id") String id) {
        if (StringUtils.isBlank(id) || !StringUtils.isNumeric(id)) {
            logger.error("Blank id");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.INVALID_OBJECT_IDENTIFIER));
        }
        CustomerRestfulBean customer = customerFacade.get(Long.parseLong(subscriber), Integer.parseInt(id));
        if (customer == null) {
            logger.error("Customer not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(value = {"", "/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomers(@PathVariable("subscriber") final String subscriber) {
        return ResponseEntity.ok().body(customerFacade.list(Long.parseLong(subscriber)));
    }

    @PostMapping(value = {"/individuals"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createIndividual(@PathVariable("subscriber") final String subscriber, @RequestBody IndividualRestBean customer) {
        if (customer == null) {
            logger.error("Individual is null");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }
            return ResponseEntity.ok().body(customerFacade.createIndividual(Long.parseLong(subscriber), IndividualMapper.builder().build().mapToDomain(customer)));
    }

    @PutMapping(value = "/individuals/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateIndividual(@PathVariable("subscriber") final String subscriber, 
                                            @PathVariable("customerId") String customerId,
                                            @RequestBody IndividualRestBean customer) {
        if (customer == null) {
            logger.error("Individual is null");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }

        if (StringUtils.isBlank(customerId) || !StringUtils.isNumeric(customerId)) {
            logger.error("Invalid customer ID");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }

        if (!StringUtils.equalsAnyIgnoreCase(customerId, customer.getId())) {
            logger.error("Customer ID is not the same as the one in the request body");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }

        return ResponseEntity.ok().body(customerFacade.updateIndividual(
            Long.parseLong(subscriber), 
            IndividualMapper.builder().build().mapToDomain(customer)
        ));
    }

    @PostMapping(value = {"/companies"}, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCompany(@PathVariable("subscriber") final String subscriber, @RequestBody CompanyRestBean company) {
        if (company == null) {
            logger.error("Company is null");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }
        return ResponseEntity.ok().body(CompanyMapper.builder().build().mapFromDomain(customerFacade.createCompany(Long.parseLong(subscriber), CompanyMapper.builder().build().mapToDomain(company))));
    }

    @PutMapping(value = "/companies/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCompany(@PathVariable("subscriber") final String subscriber, @PathVariable("customerId") String customerId, @RequestBody CompanyRestBean company) {
        if (company == null) {
            logger.error("Company is null");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }

        if (StringUtils.isBlank(customerId) || !StringUtils.isNumeric(customerId)) {
            logger.error("Invalid customer ID");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }

        if (!StringUtils.equalsAnyIgnoreCase(customerId, company.getId())) {
            logger.error("Customer ID is not the same as the one in the request body");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR));
        }

        return ResponseEntity.ok().body(CompanyMapper.builder().build().mapFromDomain(customerFacade.updateCompany(Long.parseLong(subscriber), CompanyMapper.builder().build().mapToDomain(company))));
    }
}
