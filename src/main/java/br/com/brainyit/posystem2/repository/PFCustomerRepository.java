package br.com.brainyit.posystem2.repository;

import br.com.brainyit.posystem2.domain.PFCustomer;
import br.com.brainyit.posystem2.domain.PJCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PFCustomerRepository extends JpaRepository<PFCustomer, Integer> {
}
