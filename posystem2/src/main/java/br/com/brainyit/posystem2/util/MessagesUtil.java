/**
 * 
 */
package br.com.brainyit.posystem2.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * @author rafael
 *
 */
public class MessagesUtil {

	private static Logger logger = Logger.getLogger(MessagesUtil.class);
	
	private static MessagesUtil instance;

	private static final String BUNDLE_NAME = "messages";
	
	private static final String BUNDLE_SYSTEM_PROPS_NAME = "system";
	
	//Validation static fields
	public static final String OBJECT_NOT_FOUND = "msg_onf";
	public static final String INVALID_OBJECT_IDENTIFIER = "msg_ioi";
	public static final String MESSAGE_KEY_NOT_FOUND = "msg_knf";
	public static final String OPERATION_NOT_ALLOWED = "msg_ona";
	public static final String INTERNAL_ERROR = "msg_ie";
	public static final String RESTBEAN_CONVERTION_ERROR = "msg_rce";
	

	private ResourceBundle bundle;
	
	private ResourceBundle systemPropsBundle;

	private MessagesUtil() {
		bundle = ResourceBundle.getBundle(BUNDLE_NAME);
		systemPropsBundle = ResourceBundle.getBundle(BUNDLE_SYSTEM_PROPS_NAME);
	}
	/**
	 * 
	 * @return
	 */
	public static MessagesUtil getInstance() {
		if (instance==null) {
			instance = new MessagesUtil();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getMessage(String key) {
		String message = null;
		try {
			message = this.bundle.getString(key); 
		} catch (MissingResourceException mre) {
			logger.error(mre);
		}
		return message;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getSystemProp(String key) {
		String message = null;
		try {
			message = this.systemPropsBundle.getString(key); 
		} catch (MissingResourceException mre) {
			logger.error(mre);
		}
		return message;
	}
}
