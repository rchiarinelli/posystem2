package br.com.brainyit.posystem2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "select c from Customer c where c.subscriber.id = :subscriberId and c.id = :customerId")
    List<Customer> findCustomersBySubscriberIdAndCustomerId(@Param("subscriberId") Integer subscriberId, @Param("customerId") Integer customerId);

    List<Customer> findBySubscriberId(Integer subscriberId);

}
