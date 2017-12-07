/**
 * 
 */
package br.com.brainyit.posystem2.restful;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import br.com.brainyit.posystem2.domain.User;
import br.com.brainyit.posystem2.security.interceptor.POSystemSecurityInterceptor;

/**
 * This base class provides some utility methods for RESTFul resources.
 * 
 * @author rafael
 */
public abstract class POSystemResource {
	
	protected static final String JSON_RESPONSE_FORMAT = "json";
	
	protected static final String XML_RESPONSE_FORMAT = "xml";
	
	@Context 
	private HttpServletRequest servletRequest;

	/**
	 * 
	 * @param status
	 * @param entity
	 * @param mediaType
	 * @return
	 */
	protected Response getResponse(Response.Status status, Object entity,MediaType mediaType) {
		GenericEntity<Object> genericEntity = new GenericEntity<Object>(entity,entity.getClass().getGenericSuperclass());
		return Response.status(status).entity(genericEntity).type(mediaType).build();
		
	}
	
	/**
	 * Generates a JSON response.
	 * @param status
	 * @param entity
	 * @return
	 */
	protected Response getJSONResponse(Response.Status status, Object entity) {
		GenericEntity<Object> genericEntity = new GenericEntity<Object>(entity,entity.getClass().getGenericSuperclass());
		return Response.status(status).entity(genericEntity).type(MediaType.APPLICATION_JSON).build();
	}

	/**
	 * Generates a XML response. 
	 * @param status
	 * @param entity
	 * @return
	 */
	protected Response getXMLResponse(Response.Status status, Object entity) {
		GenericEntity<Object> genericEntity = new GenericEntity<Object>(entity,
				entity.getClass().getGenericSuperclass());
		return Response.status(status).entity(genericEntity).type(MediaType.APPLICATION_XML).build();
	}
	/**
	 * Generates the {@link Response} with the status and the entity, according
	 * the provided format. If the format was not provided or unknow, a JSON response is returned.
	 * 
	 * @param status
	 * @param entity
	 * @param format
	 * @return
	 */
	public Response getResponse(Response.Status status, Object entity, String format) {
		if (StringUtils.equals(XML_RESPONSE_FORMAT, format)) {
			return this.getXMLResponse(status, entity);
		}
		return this.getJSONResponse(status, entity);
	}
	
	/**
	 * Generates the {@link Response} with the status and the entity. 
	 * 
	 * @param status
	 * @param entity
	 * @return
	 */
	public Response getResponse(Response.Status status, Object entity) {
		return this.getResponse(status, entity, StringUtils.EMPTY);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getFieldForSort() {
		Map parametersMap = this.servletRequest.getParameterMap();
		Set<String> keys = parametersMap.keySet();
		String fieldName = null;
		for (String key : keys) {
			if (key.startsWith("sort")) {
				fieldName = key.substring(key.indexOf("(")+1,key.indexOf(")")).trim();
			}
		}
		return fieldName;
	}
	/**
	 * 
	 * @return
	 */
	protected Integer getSubscriber() {
		return this.getUser().getSubscriber().getId();
	}
	/**
	 * 
	 * @return
	 */
	protected User getUser() {
		HttpSession httpSession = this.servletRequest.getSession();
		User user = (User) httpSession.getAttribute(POSystemSecurityInterceptor.AUTH_USER);
		return user;
	}
	
}
