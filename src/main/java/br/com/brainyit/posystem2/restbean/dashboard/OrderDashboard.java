/**
 *
 */
package br.com.brainyit.posystem2.restbean.dashboard;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Data;

/**
 * @author Rafael Chiarinelli
 *
 */
@Data
public class OrderDashboard extends RestBean {

    private DashboardValue open = new DashboardValue();

    private DashboardValue executing = new DashboardValue();

    private DashboardValue billed = new DashboardValue();

    private DashboardValue closed = new DashboardValue();


}
