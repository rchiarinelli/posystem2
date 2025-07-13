package br.com.brainyit.posystem2.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.Billing;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long>, QuerydslPredicateExecutor<Billing> {

}