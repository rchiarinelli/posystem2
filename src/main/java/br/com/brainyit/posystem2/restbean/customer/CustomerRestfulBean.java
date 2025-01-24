/**
 * 
 */
package br.com.brainyit.posystem2.restbean.customer;

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
public class CustomerRestfulBean extends RestBean {

	@XmlElement
	private String id;
	@XmlElement
	private String name;
	@XmlElement
	private String details;
	@XmlElement
	private String status;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
