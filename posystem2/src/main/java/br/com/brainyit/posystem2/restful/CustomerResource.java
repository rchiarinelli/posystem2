/**
 * 
 */
package br.com.brainyit.posystem2.restful;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.jboss.resteasy.annotations.GZIP;

import br.com.brainyit.filterengine.FilterCondition;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.exception.RestBeanConvertionException;
import br.com.brainyit.posystem2.facade.FilterProperties;
import br.com.brainyit.posystem2.facade.domain.CustomerFacade;
import br.com.brainyit.posystem2.facade.factory.FacadeFactoryManager;
import br.com.brainyit.posystem2.facade.filter.CustomerFilterFacade;
import br.com.brainyit.posystem2.restbean.customer.CustomerGridRownRestfulBean;
import br.com.brainyit.posystem2.restbean.customer.CustomerRestfulBean;
import br.com.brainyit.posystem2.restbean.response.Message;
import br.com.brainyit.posystem2.restful.util.JSONViewHelper;
import br.com.brainyit.posystem2.util.MessagesUtil;

/**
 * @author rafael
 *
 */
@Path("/{subscriber}/customers")
public class CustomerResource extends POSystemResource {
	
	private static final Logger logger = Logger.getLogger(CustomerResource.class);
	
	@GET
	@Path("/pf/{id}")
	@Produces("application/json")
	@GZIP
	public Response getPF(@PathParam("id")String id, @QueryParam("format") String format) {
		if (StringUtils.isBlank(id) || !StringUtils.isNumeric(id)) {
			logger.error("Blank id");
			return this.getResponse(Response.Status.BAD_REQUEST, new Message(MessagesUtil.INVALID_OBJECT_IDENTIFIER),format);
		} 
		CustomerFacade customerFacade = FacadeFactoryManager.getInstance().getCustomerFacadeFactory().createFacade();
		Integer customerId = Integer.parseInt(id);
		CustomerRestfulBean customer = customerFacade.get(customerId);
		if (customer==null) {
			logger.error("Customer not found.");
			return this.getResponse(Response.Status.NOT_FOUND, new Message(MessagesUtil.OBJECT_NOT_FOUND),format);	
		}
		return this.getResponse(Response.Status.OK, customer,format);
	}
	
	@GET
	@Produces("application/json")
	@GZIP
	public Response getCustomers(@QueryParam("format") String format){
		CustomerFilterFacade customerFilterFacade = FacadeFactoryManager.getInstance().getCustomerFacadeFactory().createFilterFacade();
		String field = this.getFieldForSort();

		List<FilterProperties> filterArgs = new ArrayList<FilterProperties>();
		filterArgs.add(new FilterProperties("customerStatus",FilterCondition.EQUALS, CustomerStatus.ACTIVE));
		
		Collection<? extends CustomerGridRownRestfulBean> restCustomerList = customerFilterFacade.filterData(this.getSubscriber(), filterArgs, field);
		JSONViewHelper helper = new JSONViewHelper();
		JSONArray resp = null;
		try {
			resp = helper.convertBeanListToJSONArray(restCustomerList);
		} catch (RestBeanConvertionException e) {
			logger.error("Customer not found.");
			return this.getResponse(Response.Status.INTERNAL_SERVER_ERROR, new Message(MessagesUtil.RESTBEAN_CONVERTION_ERROR),format);	
		}
		return this.getResponse(Response.Status.OK, resp.toString(),format);
	}

}
