/**
 * 
 */
package br.com.brainyit.posystem2.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * @author rafael
 *
 */
@Path("/{subscriber}/permissions")
public class PermissionResource extends POSystemResource {
	
	@GET
	@Path("/{systemResource}/{operation}")
	public Response get(@PathParam("systemResource") String systemResource, @PathParam("operation") String operation) {
		return this.getResponse(Response.Status.FORBIDDEN, "");
	}
}
