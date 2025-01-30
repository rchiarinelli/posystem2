/**
 *
 */
package br.com.brainyit.posystem2.customer.api;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * @author rafael
 */
@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRestfulBean extends RestBean {


    private String id;

    private String name;

    private String details;

    private String status;


}
