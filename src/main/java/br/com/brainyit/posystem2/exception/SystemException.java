/**
 * 
 */
package br.com.brainyit.posystem2.exception;

/**
 * @author rafael
 *
 */
public class SystemException extends POSystem2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6171824925501361042L;

	/**
	 * 
	 */
	public SystemException() {
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SystemException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * @param message
	 */
	public SystemException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public SystemException(Throwable cause) {
		super(cause);
		
	}

}
