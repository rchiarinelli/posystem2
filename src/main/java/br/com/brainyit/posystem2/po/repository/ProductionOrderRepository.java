package br.com.brainyit.posystem2.po.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.ProductionOrder;

@Repository
public interface ProductionOrderRepository
                extends JpaRepository<ProductionOrder, Long>, QuerydslPredicateExecutor<ProductionOrder> {

        @Query("SELECT po FROM ProductionOrder po WHERE po.subscriber.id = :subscriberId AND po.status IN (:statuses)")
        List<ProductionOrder> findBySubscriberIdAndInStatuses(@Param("subscriberId") final Long subscriberId,
                        @Param("statuses") final POStatus... statuses);

        Optional<ProductionOrder> findBySubscriberIdAndPoNumber(final Long subscriberId, final String poNumber);

}
