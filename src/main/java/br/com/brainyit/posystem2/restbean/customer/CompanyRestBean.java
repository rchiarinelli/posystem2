package br.com.brainyit.posystem2.restbean.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRestBean extends CustomerRestfulBean {

    private String cnpj;

    private String ie;

    private String im;

}
