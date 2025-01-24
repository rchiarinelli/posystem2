/**
 * 
 */
package br.com.brainyit.posystem2.restbean.response;

import java.io.Serializable;
import java.util.HashMap;

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
public class Message extends RestBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7022756619569395338L;

	@XmlElement
	private String key;
	
	@XmlElement
	private HashMap<String, String> placeHolders;

	/**
	 * 
	 */
	public Message() {
		super();
		this.placeHolders = new HashMap<String, String>();
	}
	
	
	/**
	 * @param key
	 */
	public Message(String key) {
		super();
		this.key = key;
	}


	/**
	 * @param key
	 * @param placeHolders
	 */
	public Message(String key, HashMap<String, String> placeHolders) {
		super();
		this.key = key;
		this.placeHolders = placeHolders;
	}
	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return key;
	}
	/**
	 * 
	 * @return
	 */
	public HashMap<String, String> getPlaceHolders() {
		return placeHolders;
	}
}
