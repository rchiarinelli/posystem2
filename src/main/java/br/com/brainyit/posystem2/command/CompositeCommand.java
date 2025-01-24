/**
 * 
 */
package br.com.brainyit.posystem2.command;

import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;


/**
 * @author rafael
 *
 */
public class CompositeCommand implements Command {

	private Command[] commands;
	/**
	 * 
	 * @param commands
	 */
	public CompositeCommand(Command...commands) {
		this.commands = commands;
	}
	
	/* (non-Javadoc)
	 * @see br.com.brainyit.posystem2.command.Command#execute()
	 */
	@Override
	public void execute() throws BusinessException, SystemException {
		for (Command command : this.commands) {
			command.execute();
		}		
	}

}
