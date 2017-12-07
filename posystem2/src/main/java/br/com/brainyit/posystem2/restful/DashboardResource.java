/**
 * 
 */
package br.com.brainyit.posystem2.restful;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.annotations.providers.jaxb.json.BadgerFish;

import br.com.brainyit.filterengine.Filter;
import br.com.brainyit.filterengine.FilterCondition;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.restbean.dashboard.InstallmentsByDaysRestBean;
import br.com.brainyit.posystem2.restbean.dashboard.OrderDashboard;
import br.com.brainyit.posystem2.restbean.dashboard.DashboardInstallmentsRestBean;
import br.com.brainyit.posystem2.util.FakeObjectBuilder;
import br.com.brainyit.posystem2.util.MessagesUtil;

/**
 * @author rafael
 *
 */
@Path("/{subscriber}/dashboard")
public class DashboardResource  extends POSystemResource {
	
	private static Logger logger = Logger.getLogger(DashboardResource.class);

	@GET
	@Path("/po/{fromDate}&{toDate}")
	@Produces("application/json")
	@GZIP
	public Response getPo(@PathParam("fromDate") String fromDate, @PathParam("toDate") String toDate){
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
			openReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.OPEN);
			openReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
					new Date[] { startDate, endDate });
			
			Filter execReqFilter = new Filter();
			execReqFilter.setClazz(Order.class);
			execReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.EXECUTING);
			execReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
					new Date[] { startDate, endDate });

			
			Filter billReqFilter = new Filter();
			billReqFilter.setClazz(Order.class);
			billReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.BILLED);
			billReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
					new Date[] { startDate, endDate });
			
			Filter closedReqFilter = new Filter();
			closedReqFilter.setClazz(Order.class);
			closedReqFilter.addArgument(FilterCondition.EQUALS, "status", OrderStatus.CLOSED);
			closedReqFilter.addArgument(FilterCondition.BETWEEN, "openDate",
					new Date[] { startDate, endDate });
			
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
			
			return this.getResponse(Response.Status.OK, dashboard);
		} catch (ParseException exc) {
			logger.error(exc);
			return this.getResponse(Response.Status.BAD_REQUEST, exc);
		}
	}
	
	@GET
	@Path("/installments/{days}")
	@Consumes("text/plain")
	@Produces("application/json")
	@BadgerFish	
	public Response retrievePendingInstallments(@PathParam("days")String days) {
		if (StringUtils.isBlank(days) || !StringUtils.isNumeric(days)) {
			return this.getResponse(Response.Status.BAD_REQUEST, "Param&acirc;tro inv&aacute;lido.");
		}
		
		DashboardInstallmentsRestBean installmentsResponse = new DashboardInstallmentsRestBean();
		List<InstallmentsByDaysRestBean> list = new ArrayList<InstallmentsByDaysRestBean>();
		list.add(new InstallmentsByDaysRestBean("01/01/2011", "Jao da Silva", "100,00"));
		list.add(new InstallmentsByDaysRestBean("01/01/2011", "Maria da Silva", "20,00"));
		list.add(new InstallmentsByDaysRestBean("01/01/2011", "NONOONO", "23,00"));
		
		installmentsResponse.setInstallments(list);
		
		return this.getResponse(Response.Status.OK, installmentsResponse);
	}
	
}
