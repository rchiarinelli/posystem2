package br.com.brainyit.posystem2.util;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class ApplicationEnvironmentServlet
 */
public class ApplicationEnvironmentServlet extends HttpServlet {
	
	private static Logger logger = Logger.getLogger(ApplicationEnvironmentServlet.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init();
		logger.info("======================Starting application+=======================");
		HibernateUtils.initSessionFactory("hibernate.cfg.xml");
		logger.info("==================================================================");
	}


}
