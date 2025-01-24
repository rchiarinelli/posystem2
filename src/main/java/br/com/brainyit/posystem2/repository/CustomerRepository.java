package br.com.brainyit.posystem2.repository;

import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.PFCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
