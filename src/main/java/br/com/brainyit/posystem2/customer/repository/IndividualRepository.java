package br.com.brainyit.posystem2.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Individual;

@Repository
public interface IndividualRepository extends JpaRepository<Individual, Integer> {

    Individual findBySubscriberIdAndCpfAndName(final Long subscriberId, final String cpf, final String name);

    Individual findByIdAndSubscriberId(final Integer id, final Long subscriberId);
}
