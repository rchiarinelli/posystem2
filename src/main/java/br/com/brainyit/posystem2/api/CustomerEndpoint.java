/**
 *
 */
package br.com.brainyit.posystem2.api;

import br.com.brainyit.posystem2.facade.domain.CustomerFacade;
import br.com.brainyit.posystem2.facade.filter.CustomerFilterFacade;
import br.com.brainyit.posystem2.restbean.customer.CustomerRestfulBean;
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
        CustomerRestfulBean customer = customerFacade.get(Integer.parseInt(subscriber), Integer.parseInt(id));
        if (customer == null) {
            logger.error("Customer not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping( value = {"","/"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomers(@PathVariable("subscriber") final String subscriber) {
        return ResponseEntity.ok().body(customerFacade.list(Integer.parseInt(subscriber)));
    }

}
