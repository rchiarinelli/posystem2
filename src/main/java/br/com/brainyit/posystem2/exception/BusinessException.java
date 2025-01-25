/**
 * 
 */
package br.com.brainyit.posystem2.exception;

/**
 * @author rafael
 *
 */
public class BusinessException extends POSystem2Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7102766927090477939L;

	/**
	 * 
	 */
	public BusinessException() {
		
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * @param message
	 */
	public BusinessException(String message) {
		super(message);
		
	}

	/**
	 * @param cause
	 */
	public BusinessException(Throwable cause) {
		super(cause);
		
	}

}
