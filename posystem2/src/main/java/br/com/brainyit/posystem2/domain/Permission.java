/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author leafar
 *
 */
@Entity
@Table(name="t_permission")
public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1098354854369360899L;

	@Id
	private PermissionKey componsiteId;

	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((componsiteId == null) ? 0 : componsiteId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Permission other = (Permission) obj;
		if (componsiteId == null) {
			if (other.componsiteId != null)
				return false;
		} else if (!componsiteId.equals(other.componsiteId))
			return false;
		return true;
	}

	/**
	 * @return the componsiteId
	 */
	public PermissionKey getComponsiteId() {
		return componsiteId;
	}

	/**
	 * @param componsiteId the componsiteId to set
	 */
	public void setComponsiteId(PermissionKey componsiteId) {
		this.componsiteId = componsiteId;
	}
}
