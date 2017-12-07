package br.com.brainyit.posystem2.restful;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.jboss.resteasy.annotations.GZIP;

import br.com.brainyit.posystem2.restbean.response.Message;
import br.com.brainyit.posystem2.restbean.response.MessageValue;
import br.com.brainyit.posystem2.util.MessagesUtil;

/**
 * 
 * @author rafael
 *
 */
@Path("/{subscriber}/messages")
public class MessageResource extends POSystemResource {

	/**
	 * 
	 * @param key
	 * @return
	 */
	@GET
	@Path("/{key}")
	@Produces("application/json")
	@GZIP
	public Response get(@PathParam("key") String key,@QueryParam("format") String format) {
		if (StringUtils.isBlank(key)) {
			return this.getResponse(Response.Status.BAD_REQUEST, new Message(MessagesUtil.INVALID_OBJECT_IDENTIFIER),format);
		}
		String msg = MessagesUtil.getInstance().getMessage(key);
		if (msg==null) {
			return this.getResponse(Response.Status.NOT_FOUND, new Message(MessagesUtil.MESSAGE_KEY_NOT_FOUND),format);	
		}
		return this.getResponse(Response.Status.OK, new MessageValue(msg),format);
	}
}
