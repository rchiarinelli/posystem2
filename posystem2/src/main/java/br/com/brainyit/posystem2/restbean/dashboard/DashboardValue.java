/**
 * 
 */
package br.com.brainyit.posystem2.restbean.dashboard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * @author Rafael Chiarinelli
 *
 */
@XmlRootElement(name="dashboardValue")
@XmlAccessorType(XmlAccessType.FIELD)
public class DashboardValue extends RestBean {

	@XmlElement(name="value")
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
