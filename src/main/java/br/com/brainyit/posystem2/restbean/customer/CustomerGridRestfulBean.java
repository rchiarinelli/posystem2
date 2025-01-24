/**
 * 
 */
package br.com.brainyit.posystem2.restbean.customer;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * @author rafael
 *
 */

@XmlRootElement(name="grid")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerGridRestfulBean extends RestBean {
	
	
	@XmlElement
	private List<CustomerGridRownRestfulBean> items;

	/**
	 * 
	 */
	public CustomerGridRestfulBean() {
	}

	/**
	 * @param items
	 */
	public CustomerGridRestfulBean(List<CustomerGridRownRestfulBean> items) {
		this.items = items;
	}

	/**
	 * @return the items
	 */
	public List<CustomerGridRownRestfulBean> getItems() {
		return items;
	}

	/**
	 * @param items the items to set
	 */
	public void setItems(List<CustomerGridRownRestfulBean> items) {
		this.items = items;
	}
}
