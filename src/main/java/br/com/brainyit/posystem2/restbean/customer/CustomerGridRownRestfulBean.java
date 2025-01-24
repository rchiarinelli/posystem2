/**
 *
 */
package br.com.brainyit.posystem2.restbean.customer;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rafael
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerGridRownRestfulBean extends RestBean {

    private Integer cust_id;

    private String name;

    private String custIdent;

    private String typeName;

    private String type;


}
