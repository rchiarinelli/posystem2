package br.com.brainyit.posystem2.repository;

import br.com.brainyit.posystem2.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PJCustomerRepository extends JpaRepository<Company, Integer> {
}
