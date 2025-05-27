package br.com.brainyit.posystem2.customer.mapper;

import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.model.IndividualRestModel;
import lombok.Builder;

@Builder
public class IndividualMapper {

    public IndividualRestModel mapFromDomain(final Individual individual) {
        if (individual == null) {
            return null;
        }

        final IndividualRestModel individualRestModel = new IndividualRestModel();
        individualRestModel.setCpf(individual.getCpf());
        individualRestModel.setRg(individual.getRg());

        individualRestModel.setId(individual.getId().toString());
        individualRestModel.setName(individual.getName());;
        individualRestModel.setDetails(individual.getDetails());;
        individualRestModel.setStatus(individual.getStatus().name());;

        return individualRestModel;
    }

    public Individual mapToDomain(final IndividualRestModel restBean) {
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
