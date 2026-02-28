package br.com.brainyit.posystem2.po.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.com.brainyit.posystem2.domain.WorkSchedule;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long>, QuerydslPredicateExecutor<WorkSchedule> {

}
