package br.com.brainyit.posystem2.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Classe utilitaria usada para recuperar a sessao a partir {@link SessionFactory}.
 * 
 * @author Rafael Chiairnelli
 *
 */
public class HibernateUtils {
	
	private static Logger logger = Logger.getLogger(HibernateUtils.class);
	
	private static SessionFactory sessionFactory;
	
	private static Configuration configuration;
	
    /**
     * 
     */
    public static void initSessionFactory(String fileName) {
    	if (sessionFactory==null) {
            try {
            	configuration = new Configuration();
            	sessionFactory = configuration.configure(fileName).buildSessionFactory();
            } catch (Throwable ex) {
            	logger.error(ex);
                throw new ExceptionInInitializerError(ex);
            }
    	}
    }
    
    /**
     * 
     * @return
     */
    public synchronized static SessionFactory getSessionFactory() {
    	return sessionFactory;
    }

    
}
