package br.com.brainyit.posystem2.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Company findBySubscriberIdAndCnpjCgcAndName(final Long subscriberId, final String cnpjCgc, final String name);

}
