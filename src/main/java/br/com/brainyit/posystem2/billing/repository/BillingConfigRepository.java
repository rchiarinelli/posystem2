package br.com.brainyit.posystem2.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.brainyit.posystem2.domain.BillingConfig;

@Repository
public interface BillingConfigRepository extends JpaRepository<BillingConfig, Integer> {

}