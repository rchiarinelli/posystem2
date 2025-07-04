package br.com.brainyit.posystem2.po.service;

import java.time.LocalDate;

import br.com.brainyit.posystem2.domain.POPriority;

public record CreateProductionOrderParams(
        String orderCode,
        String instructions,
        Double qtd,
        Double estimatedTime,
        LocalDate plannedStartDate,
        POPriority priority) {
}
