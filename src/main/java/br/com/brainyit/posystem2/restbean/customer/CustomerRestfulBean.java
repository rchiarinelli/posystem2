/**
 *
 */
package br.com.brainyit.posystem2.restbean.customer;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author rafael
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerRestfulBean extends RestBean {


    private String id;

    private String name;

    private String details;

    private String status;


}
