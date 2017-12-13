package br.com.brainyit.posystem2.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ApplicationEnvironmentServlet
 */
@WebListener
public class ApplicationEnvironmentServlet implements ServletContextListener {
	
	private static Logger logger = Logger.getLogger(ApplicationEnvironmentServlet.class);
	
	private static final long serialVersionUID = 1L;

	

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("======================Starting application+=======================");
		HibernateUtils.initSessionFactory("hibernate.cfg.xml");
		logger.info("==================================================================");	
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}


}
