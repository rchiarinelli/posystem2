/**
 *
 */
package br.com.brainyit.posystem2.restbean.customer;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author rafael
 *
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerGridRestfulBean extends RestBean {

    private List<CustomerGridRownRestfulBean> items;
}
