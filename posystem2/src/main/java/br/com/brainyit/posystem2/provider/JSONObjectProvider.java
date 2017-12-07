/**
 * 
 */
package br.com.brainyit.posystem2.provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.jaxb.AbstractJAXBProvider;

import br.com.brainyit.posystem2.restful.util.JSONGridObject;

/**
 * @author rafael
 *
 */
//@Provider
//@Produces("application/*+json")
//@Consumes("application/*+json")
public class JSONObjectProvider extends AbstractJAXBProvider<JSONGridObject> {

	
	/* (non-Javadoc)
	 * @see org.jboss.resteasy.plugins.providers.jaxb.AbstractJAXBProvider#isWriteable(java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType)
	 */
	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return type==JSONGridObject.class && mediaType.isCompatible(MediaType.APPLICATION_JSON_TYPE);
	}

	@Override
	protected boolean isReadWritable(Class<?> arg0, Type arg1,
			Annotation[] arg2, MediaType arg3) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.jboss.resteasy.plugins.providers.jaxb.AbstractJAXBProvider#writeTo(java.lang.Object, java.lang.Class, java.lang.reflect.Type, java.lang.annotation.Annotation[], javax.ws.rs.core.MediaType, javax.ws.rs.core.MultivaluedMap, java.io.OutputStream)
	 */
	@Override
	public void writeTo(JSONGridObject t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream outputStream) throws IOException {
		outputStream.write(t.toString().getBytes());
		outputStream.flush();
		//super.writeTo(t, type, genericType, annotations, mediaType, httpHeaders, outputStream);
	}
}
