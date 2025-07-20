package br.com.brainyit.posystem2.billing.mapper;

import java.util.List;
import java.util.stream.Collectors;

import br.com.brainyit.posystem2.domain.BillingConfig;
import br.com.brainyit.posystem2.model.BillingConfigModel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class BillingConfigMapper {

    public BillingConfigModel toRestModel(BillingConfig billingConfig) {
        if (billingConfig == null) {
            return null;
        }

        log.debug("Converting BillingConfig entity to BillingConfigModel: {}", billingConfig.getId());

        BillingConfigModel model = new BillingConfigModel();
        model.setId(billingConfig.getId());
        model.setName(billingConfig.getName());
        model.setPaymentType(billingConfig.getPaymentType() != null ? billingConfig.getPaymentType().name() : null);
        model.setInstallmentsQuantity(
                billingConfig.getInstallmentsQuantity() != null ? billingConfig.getInstallmentsQuantity().intValue()
                        : null);
        model.setInstallmentsInterval(
                billingConfig.getInstallmentsInterval() != null ? billingConfig.getInstallmentsInterval().intValue()
                        : null);
        model.setInstallmentsIntervalUnit(
                billingConfig.getInstallmentIntervalUnit() != null ? billingConfig.getInstallmentIntervalUnit().name()
                        : null);

        return model;
    }

    public List<BillingConfigModel> toRestModelList(List<BillingConfig> billingConfigs) {
        if (billingConfigs == null || billingConfigs.isEmpty()) {
            return List.of();
        }

        return billingConfigs.stream()
                .map(this::toRestModel)
                .collect(Collectors.toList());
    }
}