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
@Entity(name = "t_company")
@PrimaryKeyJoinColumn(name = "id_customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Company extends Customer {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -5514879621621285962L;

    @Column(name = "cnpj_cgc", nullable = false, length = 18)
    private String cnpjCgc;

    @Column(name = "inscr_estadual", nullable = true)
    private String inscrEstadual;

    @Column(name = "inscr_municipal", nullable = true)
    private String inscrMunicipal;
}
