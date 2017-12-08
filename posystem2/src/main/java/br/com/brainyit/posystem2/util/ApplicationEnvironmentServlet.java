package br.com.brainyit.posystem2.util;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ApplicationEnvironmentServlet
 */
@HandlesTypes(Object.class)
public class ApplicationEnvironmentServlet implements ServletContainerInitializer {
	
	private static Logger logger = Logger.getLogger(ApplicationEnvironmentServlet.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		logger.info("======================Starting application+=======================");
		HibernateUtils.initSessionFactory("hibernate.cfg.xml");
		logger.info("==================================================================");
		
	}


}
