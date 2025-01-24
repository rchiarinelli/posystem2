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
@XmlRootElement(name="orderValues")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderDashboard extends RestBean {
	@XmlElement
	private DashboardValue open;
	@XmlElement
	private DashboardValue executing;
	@XmlElement
	private DashboardValue billed;
	@XmlElement
	private DashboardValue closed;

	
	
	public OrderDashboard() {
		super();
		this.open = new DashboardValue();
		this.executing = new DashboardValue();
		this.billed = new DashboardValue();
		this.closed = new DashboardValue();
	}

	public DashboardValue getOpen() {
		return open;
	}

	public void setOpen(DashboardValue open) {
		this.open = open;
	}

	public DashboardValue getExecuting() {
		return executing;
	}

	public void setExecuting(DashboardValue executing) {
		this.executing = executing;
	}

	public DashboardValue getBilled() {
		return billed;
	}

	public void setBilled(DashboardValue billed) {
		this.billed = billed;
	}

	/**
	 * @return the closed
	 */
	public DashboardValue getClosed() {
		return closed;
	}

	/**
	 * @param closed the closed to set
	 */
	public void setClosed(DashboardValue closed) {
		this.closed = closed;
	}
	
	
}
