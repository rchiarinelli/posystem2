/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leafar
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_permission")
public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1098354854369360899L;

	@Id
	private PermissionKey componsiteId;
}
