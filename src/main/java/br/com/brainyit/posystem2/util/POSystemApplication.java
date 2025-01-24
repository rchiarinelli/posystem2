/**
 * 
 */
package br.com.brainyit.posystem2.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import br.com.brainyit.posystem2.restful.CustomerResource;
import br.com.brainyit.posystem2.restful.DashboardResource;
import br.com.brainyit.posystem2.restful.MessageResource;
import br.com.brainyit.posystem2.restful.PermissionResource;

/**
 * @author rchiari
 *
 */
@ApplicationPath("/services")
public class POSystemApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new HashSet<>();
		resources.add(CustomerResource.class);
		resources.add(DashboardResource.class);
		resources.add(MessageResource.class);
		resources.add(PermissionResource.class);

		return Collections.emptySet();
	}
}
