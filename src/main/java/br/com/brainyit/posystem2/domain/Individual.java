/**
 *
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author leafar
 *
 */
@Entity(name = "t_individual")
@PrimaryKeyJoinColumn(name = "id_customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Individual extends Customer {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1541797716230550445L;

    @Column(name = "cpf", nullable = false, length = 14)
    private String cpf;

    @Column(name = "rg", nullable = true)
    private String rg;

}
