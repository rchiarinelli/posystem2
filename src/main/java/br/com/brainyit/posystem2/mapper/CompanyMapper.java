package br.com.brainyit.posystem2.mapper;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.CustomerStatus;
import br.com.brainyit.posystem2.restbean.customer.CompanyRestBean;
import lombok.Builder;

@Builder
public class CompanyMapper {

    public CompanyRestBean mapFromDomain(final Company company) {
        if (company == null) {
            return null;
        }

        return CompanyRestBean.builder()
                .id(company.getId().toString())
                .name(company.getName())
                .details(company.getDetails())
                .status(company.getStatus().toString())
                .cnpj(company.getCnpjCgc())
                .ie(company.getInscrEstadual())
                .im(company.getInscrMunicipal())
                .build();
    }

    public Company mapToDomain(final CompanyRestBean restBean) {
        if (restBean == null) {
            return null;
        }

        return Company.builder()
                .id(restBean.getId() != null ? Integer.valueOf(restBean.getId()) : null)
                .name(restBean.getName())
                .details(restBean.getDetails())
                .status(restBean.getStatus() != null ? CustomerStatus.valueOf(restBean.getStatus()) : null)
                .cnpjCgc(restBean.getCnpj())
                .inscrEstadual(restBean.getIe())
                .inscrMunicipal(restBean.getIm())
                .build();
    }

}
