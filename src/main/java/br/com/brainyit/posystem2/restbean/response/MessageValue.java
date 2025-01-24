/**
 * 
 */
package br.com.brainyit.posystem2.restbean.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * @author rafael
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageValue extends RestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3394437015115248676L;
	
	@XmlElement
	private String value;

	/**
	 * 
	 */
	public MessageValue() {
		super();
	}
	
	

	/**
	 * @param value
	 */
	public MessageValue(String value) {
		super();
		this.value = value;
	}



	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
