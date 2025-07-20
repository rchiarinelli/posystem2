package br.com.brainyit.posystem2.billing.service;

import java.time.LocalDate;

public record CreateBillingParams(
        String orderCode,
        int billingConfigId,
        LocalDate startDate,
        Double value) {
}