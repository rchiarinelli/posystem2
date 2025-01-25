/**
 *
 */
package br.com.brainyit.posystem2.restbean.dashboard;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;

import java.util.List;

/**
 * @author rchiari
 *
 */
@Data
public class DashboardInstallmentsRestBean extends RestBean {

    private List<InstallmentsByDaysRestBean> installments;

}
