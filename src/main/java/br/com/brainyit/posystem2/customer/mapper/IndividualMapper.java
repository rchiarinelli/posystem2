package br.com.brainyit.posystem2.customer.mapper;

import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.customer.api.IndividualRestBean;
import lombok.Builder;

@Builder
public class IndividualMapper {

    public IndividualRestBean mapFromDomain(final Individual individual) {
        if (individual == null) {
            return null;
        }

        return IndividualRestBean.builder()
                .id(individual.getId().toString())
                .name(individual.getName())
                .details(individual.getDetails())
                .status(individual.getStatus().toString())
                .cpf(individual.getCpf())
                .rg(individual.getRg())
                .build();
    }

    public Individual mapToDomain(final IndividualRestBean restBean) {
        if (restBean == null) {
            return null;
        }

        return Individual.builder()
                .id(restBean.getId() != null ? Integer.valueOf(restBean.getId()) : null)
                .name(restBean.getName())
                .details(restBean.getDetails()) 
                .status(restBean.getStatus() != null ? CustomerStatus.valueOf(restBean.getStatus()) : null)
                .cpf(restBean.getCpf())
                .rg(restBean.getRg())
                .build();
    }
}
