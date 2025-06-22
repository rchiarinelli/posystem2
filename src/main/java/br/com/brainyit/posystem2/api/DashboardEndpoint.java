/**
 *
 */
package br.com.brainyit.posystem2.api;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.filterengine.FilterCondition;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.restbean.dashboard.DashboardInstallmentsRestBean;
import br.com.brainyit.posystem2.restbean.dashboard.InstallmentsByDaysRestBean;
import br.com.brainyit.posystem2.restbean.dashboard.OrderDashboard;
import br.com.brainyit.posystem2.util.FakeObjectBuilder;
import br.com.brainyit.posystem2.util.MessagesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author rafael
 */
@RestController
@RequestMapping("/{subscriber}/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardEndpoint {


    @GetMapping(value = "/po", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPo(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
        String dashboardDatePattern = MessagesUtil.getInstance().getSystemProp("dashboard_date_pattern");
        DateFormat df = new SimpleDateFormat(dashboardDatePattern);

        String startDateStr = StringUtils.replace(fromDate, "-", "/");
        String endDateStr = StringUtils.replace(toDate, "-", "/");

        try {

            Date startDate = df.parse(startDateStr);
            Date endDate = df.parse(endDateStr);

            df = new SimpleDateFormat(MessagesUtil.getInstance().getSystemProp("filter_date_pattern"));

            Filter openReqFilter = new Filter();
            openReqFilter.setClazz(Order.class);
            openReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.PENDING);
            openReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
                    new Date[]{startDate, endDate});

            Filter execReqFilter = new Filter();
            execReqFilter.setClazz(Order.class);
            execReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.EXECUTING);
            execReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
                    new Date[]{startDate, endDate});


            Filter billReqFilter = new Filter();
            billReqFilter.setClazz(Order.class);
            billReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.BILLED);
            billReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
                    new Date[]{startDate, endDate});

            Filter closedReqFilter = new Filter();
            closedReqFilter.setClazz(Order.class);
            closedReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.CLOSED);
            closedReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
                    new Date[]{startDate, endDate});

            //Executar os filtros
            List<Order> openReqResults = FakeObjectBuilder.buildOrderList();
            List<Order> exeReqResults = FakeObjectBuilder.buildOrderList();
            List<Order> billReqResults = FakeObjectBuilder.buildOrderList();
            List<Order> closedReqResults = FakeObjectBuilder.buildOrderList();


            OrderDashboard dashboard = new OrderDashboard();
            dashboard.getBilled().setValue(String.valueOf(billReqResults.size()));
            dashboard.getExecuting().setValue(String.valueOf(exeReqResults.size()));
            dashboard.getOpen().setValue(String.valueOf(openReqResults.size()));
            dashboard.getClosed().setValue(String.valueOf(closedReqResults.size()));

            return ResponseEntity.ok(dashboard);
        } catch (ParseException exc) {
            log.error(exc.getMessage(), exc);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/installments/{days}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> retrievePendingInstallments(@PathVariable("days") String days) {
        if (StringUtils.isBlank(days) || !StringUtils.isNumeric(days)) {
            return ResponseEntity.badRequest().body("Param&acirc;tro inv&aacute;lido.");
        }

        DashboardInstallmentsRestBean installmentsResponse = new DashboardInstallmentsRestBean();

        installmentsResponse.setInstallments(List.of(
                InstallmentsByDaysRestBean.builder().date("01/01/2011").customerName("Jao da Silva").value("100,00").build(),
                InstallmentsByDaysRestBean.builder().date("01/01/2011").customerName("Maria").value("20,00").build(),
                InstallmentsByDaysRestBean.builder().date("01/01/2011").customerName("BBIBNNONO").value("23,00").build()));

        return ResponseEntity.ok(installmentsResponse);
    }

}
