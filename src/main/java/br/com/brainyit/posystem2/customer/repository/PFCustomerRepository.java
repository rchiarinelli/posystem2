package br.com.brainyit.posystem2.customer.repository;

import br.com.brainyit.posystem2.domain.Individual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PFCustomerRepository extends JpaRepository<Individual, Integer> {
}
