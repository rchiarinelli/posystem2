package br.com.brainyit.posystem2.util;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Classe utilitaria usada para recuperar a sessao a partir {@link SessionFactory}.
 * 
 * @author Rafael Chiairnelli
 *
 */
public class HibernateUtils {
	
	private static Logger logger = Logger.getLogger(HibernateUtils.class);
	
	private static SessionFactory sessionFactory;
	
	private static AnnotationConfiguration configuration;
	
    /**
     * 
     */
    public static void initSessionFactory(String fileName) {
    	if (sessionFactory==null) {
            try {
            	configuration = new AnnotationConfiguration();
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
