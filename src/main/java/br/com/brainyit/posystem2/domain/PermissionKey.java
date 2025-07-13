/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leafar
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class PermissionKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2776481680689603378L;

	/**
	 * Enum com as operacoes disponiveis no sistema.
	 * 
	 * @author leafar
	 *
	 */
	public enum Operation {
		ADD("add"), ERASE("erase"), READ("read"), UPDATE("update");

		private String opValue;

		private Operation(String value) {
			opValue = value;
		}

		@Override
		public String toString() {
			return this.opValue;
		}
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_login", insertable = false, updatable = false)
	private User user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_resource", insertable = false, updatable = false)
	private Resource resource;

	@Column(name = "operation_name")
	@Enumerated(EnumType.STRING)
	private Operation operation;
}
