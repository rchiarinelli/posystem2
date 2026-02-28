package br.com.brainyit.posystem2.po.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import br.com.brainyit.posystem2.domain.WorkResource;

public interface WorkResourceRepository
        extends JpaRepository<WorkResource, Long>, QuerydslPredicateExecutor<WorkResource> {

    @Query("select wr from WorkResource wr LEFT JOIN wr.workAllocations wa WHERE wr.subscriber.id = :subscriberId AND wa.subscriber.id = :subscriberId AND wa.startDateTime >= :fromDateTime")
    List<WorkResource> retrieveFreeWorkResourceByDate(@Param("subscriberId") Long subscriberId, @Param("fromDateTime") final LocalDateTime fromDateTime);

}
