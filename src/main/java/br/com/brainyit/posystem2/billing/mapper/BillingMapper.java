package br.com.brainyit.posystem2.billing.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.brainyit.posystem2.domain.Billing;
import br.com.brainyit.posystem2.domain.Installment;
import br.com.brainyit.posystem2.model.BillingModel;
import br.com.brainyit.posystem2.model.InstallmentModel;
import br.com.brainyit.posystem2.model.OrderRestModel;
import br.com.brainyit.posystem2.order.mapper.OrderMapper;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class BillingMapper {

    public BillingModel toRestModel(Billing billing) {
        if (billing == null) {
            return null;
        }

        log.debug("Converting Billing entity to BillingModel: {}", billing.getUuid());

        BillingModel model = new BillingModel();
        model.setUuid(billing.getUuid());
        model.setCreatedAt(billing.getCreatedAt() != null ? billing.getCreatedAt().toString() : null);
        model.setClosedAt(billing.getClosedAt() != null ? billing.getClosedAt().toString() : null);
        model.setStatus(billing.getStatus() != null ? billing.getStatus().name() : null);
        model.setCalculatedValue(billing.getCalculatedValue());
        model.setFinalValue(billing.getFinalValue());

        // Convert installments
        if (billing.getInstallments() != null && !billing.getInstallments().isEmpty()) {
            List<InstallmentModel> installments = billing.getInstallments().stream()
                    .map(this::toInstallmentModel)
                    .collect(Collectors.toList());
            model.setInstallments(installments);
        }

        // Convert order
        if (billing.getOrder() != null) {
            OrderMapper orderMapper = OrderMapper.builder().build();
            OrderRestModel orderModel = orderMapper.toRestBean(billing.getOrder());
            model.setOrder(orderModel);
        }

        return model;
    }

    public InstallmentModel toInstallmentModel(Installment installment) {
        if (installment == null) {
            return null;
        }

        InstallmentModel model = new InstallmentModel();
        model.setUuid(installment.getUuid());
        model.setDueValue(installment.getDueValue());
        model.setDueDate(installment.getDueDate() != null ? installment.getDueDate().toString() : null);
        model.setCreatedAt(installment.getCreatedAt() != null ? installment.getCreatedAt().toString() : null);
        model.setPaymentDate(installment.getPaymentDate() != null ? installment.getPaymentDate().toString() : null);
        model.setPaidValue(installment.getPaidValue());
        model.setStatus(installment.getStatus() != null ? installment.getStatus().name() : null);

        return model;
    }

    public List<BillingModel> toRestModelList(List<Billing> billings) {
        if (billings == null || billings.isEmpty()) {
            return List.of();
        }

        return billings.stream()
                .map(this::toRestModel)
                .collect(Collectors.toList());
    }
}