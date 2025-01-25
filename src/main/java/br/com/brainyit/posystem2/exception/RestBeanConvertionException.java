/**
 * 
 */
package br.com.brainyit.posystem2.exception;

/**
 * @author rafael
 *
 */
public class RestBeanConvertionException extends POSystem2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212925456946513016L;

	/**
	 * 
	 */
	public RestBeanConvertionException() {
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RestBeanConvertionException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * @param message
	 */
	public RestBeanConvertionException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public RestBeanConvertionException(Throwable cause) {
		super(cause);
		
	}

}
