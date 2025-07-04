package br.com.brainyit.posystem2.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

    Optional<Order> findBySubscriberIdAndOrderCode(final Long subscriberId, final String orderCode);

    List<Order> findBySubscriberId(final Long subscriberId);

}
