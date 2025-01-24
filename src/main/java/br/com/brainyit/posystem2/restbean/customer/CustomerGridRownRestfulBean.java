/**
 * 
 */
package br.com.brainyit.posystem2.restbean.customer;

import javax.xml.bind.annotation.XmlRootElement;

import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * @author rafael
 *
 */
@XmlRootElement
public class CustomerGridRownRestfulBean extends RestBean {
	
	
	private Integer cust_id;
	
	private String name;
	
	private String custIdent;
	
	private String typeName;
	
	private String type;
	
	/**
	 * 
	 */
	public CustomerGridRownRestfulBean() {
	}
	
	

	/**
	 * @param id
	 * @param name
	 * @param custIdent
	 * @param typeName
	 * @param type
	 */
	public CustomerGridRownRestfulBean(Integer id, String name,
			String custIdent, String typeName, String type) {
		this.cust_id = id;
		this.name = name;
		this.custIdent = custIdent;
		this.typeName = typeName;
		this.type = type;
	}



	/**
	 * @return the id
	 */
	public Integer getCust_id() {
		return cust_id;
	}

	/**
	 * @param id the id to set
	 */
	public void setCust_id(Integer id) {
		this.cust_id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the custIdent
	 */
	public String getCustIdent() {
		return custIdent;
	}

	/**
	 * @param custIdent the custIdent to set
	 */
	public void setCustIdent(String custIdent) {
		this.custIdent = custIdent;
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
