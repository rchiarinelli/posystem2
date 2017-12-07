/**
 * 
 */
package br.com.brainyit.posystem2.restbean.dashboard;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.brainyit.posystem2.restbean.RestBean;

/**
 * @author rchiari
 *
 */
@XmlRootElement(name="pendingInstallments")
@XmlAccessorType(XmlAccessType.FIELD)
public class DashboardInstallmentsRestBean extends RestBean {

	@XmlElement
	private List<InstallmentsByDaysRestBean> installments;

	/**
	 * @return the installments
	 */
	public List<InstallmentsByDaysRestBean> getInstallments() {
		return installments;
	}

	/**
	 * @param installments the installments to set
	 */
	public void setInstallments(List<InstallmentsByDaysRestBean> installments) {
		this.installments = installments;
	}
	
	
	
}
