package br.com.brainyit.posystem2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Subscriber;

/**
 * 
 * @author Rafael Chiarinelli
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}
