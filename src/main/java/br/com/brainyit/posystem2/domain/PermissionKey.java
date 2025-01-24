/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author leafar
 *
 */
@Embeddable
public class PermissionKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2776481680689603378L;	
	
	/**
	 * Enum com as operacoes disponiveis no sistema.
	 * @author leafar
	 *
	 */
	public enum Operation {
		ADD("add"),ERASE("erase"),READ("read"),UPDATE("update");
		
		private String opValue;
		
		private Operation(String value) {
			opValue = value;
		}
		
		@Override
		public String toString() {
			return this.opValue;
		}
	}
	
	
	
	/**
	 * 
	 */
	public PermissionKey() {
		super();
	}

	/**
	 * @param user
	 * @param resource
	 * @param operation
	 */
	public PermissionKey(User user, Resource resource, Operation operation) {
		super();
		this.user = user;
		this.resource = resource;
		this.operation = operation;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		PermissionKey other = (PermissionKey) obj;
		if (operation != other.operation)
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}



	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_login",insertable=false,updatable=false)
	private User user;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_resource",insertable=false,updatable=false)
	private Resource resource;

	@Column(name="operation_name")
	@Enumerated(EnumType.STRING)
	private Operation operation;

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @return the resource
	 */
	public Resource getResource() {
		return resource;
	}

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
