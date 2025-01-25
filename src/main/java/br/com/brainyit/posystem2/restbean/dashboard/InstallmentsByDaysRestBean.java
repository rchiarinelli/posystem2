/**
 *
 */
package br.com.brainyit.posystem2.restbean.dashboard;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Builder;
import lombok.Data;

/**
 * @author rchiari
 *
 */
@Data
@Builder
public class InstallmentsByDaysRestBean extends RestBean {

    private String id;

    private String date;

    private String customerName;

    private String value;
}
