package br.com.brainyit.posystem2.customer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

        @Query(value = "select c from Customer c where c.subscriber.id = :subscriberId and c.id = :customerId")
        List<Customer> findCustomersBySubscriberIdAndCustomerId(@Param("subscriberId") Long subscriberId,
                        @Param("customerId") Integer customerId);

        @Query(value = "select c from Customer c where c.subscriber.uuid = :subscriberUuid and c.id = :customerId")
        List<Customer> findCustomersBySubscriberUuidAndCustomerId(@Param("subscriberUuid") String subscriberUuid,
                        @Param("customerId") Integer customerId);

        List<Customer> findBySubscriberUuid(String subscriberUuid);

}
