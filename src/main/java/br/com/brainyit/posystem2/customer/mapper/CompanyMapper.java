package br.com.brainyit.posystem2.customer.mapper;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.model.CompanyRestModel;
import lombok.Builder;

@Builder
public class CompanyMapper {

    public CompanyRestModel mapFromDomain(final Company company) {
        if (company == null) {
            return null;
        }

        final CompanyRestModel companyRestModel = new CompanyRestModel();
        companyRestModel.setCnpj(company.getCnpjCgc());
        companyRestModel.setInscricaoEstadual(company.getInscrEstadual());
        companyRestModel.setInscricaoMunicipal(company.getInscrMunicipal());

        companyRestModel.setId(company.getId().toString());
        companyRestModel.setName(company.getName());;
        companyRestModel.setDetails(company.getDetails());;
        companyRestModel.setStatus(company.getStatus().name());;

        return companyRestModel;
    }

    public Company mapToDomain(final CompanyRestModel restBean) {
        if (restBean == null) {
            return null;
        }

        return Company.builder()
                .id(restBean.getId() != null ? Integer.valueOf(restBean.getId()) : null)
                .name(restBean.getName())
                .details(restBean.getDetails())
                .status(restBean.getStatus() != null ? CustomerStatus.valueOf(restBean.getStatus()) : null)
                .cnpjCgc(restBean.getCnpj())
                .inscrEstadual(restBean.getInscricaoEstadual())
                .inscrMunicipal(restBean.getInscricaoMunicipal())
                .build();
    }

}
