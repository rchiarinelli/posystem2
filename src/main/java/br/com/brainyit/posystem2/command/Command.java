/**
 * 
 */
package br.com.brainyit.posystem2.command;

import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;

/**
 * 
 * 
 * @author rafael
 *
 */
public interface Command {
	/**
	 * 
	 * @param command
	 * @return
	 * throws BusinessException, SystemException
	 */
	public void execute() throws BusinessException, SystemException;

}
