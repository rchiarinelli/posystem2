/**
 *
 */
package br.com.brainyit.posystem2.restful;

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
public class CustomerResource {

    private static final Logger logger = LogManager.getLogger(CustomerResource.class);

    private final CustomerFacade customerFacade;

    private final CustomerFilterFacade customerFilterFacade;

    @GetMapping(value = "/pf/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPF(@PathVariable("subscriber") final String subscriber, @RequestParam("id") String id, @RequestParam("format") String format) {
        if (StringUtils.isBlank(id) || !StringUtils.isNumeric(id)) {
            logger.error("Blank id");
            return ResponseEntity.badRequest().body(new Message(MessagesUtil.INVALID_OBJECT_IDENTIFIER));
        }
        CustomerRestfulBean customer = customerFacade.get(Integer.parseInt(id));
        if (customer == null) {
            logger.error("Customer not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(customer);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomers(@PathVariable("subscriber") final String subscriber) {
        return ResponseEntity.ok().body(customerFilterFacade.filterData(Integer.parseInt(subscriber), null, null));
    }

}
