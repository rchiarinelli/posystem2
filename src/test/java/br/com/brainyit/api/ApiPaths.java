package br.com.brainyit.api;

public class ApiPaths {

    private ApiPaths() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ORDERS_ENDPOINT_PATH = "/{subscriberId}/orders";

    public static final String ORDER_ENDPOINT_PATH = "/{subscriberId}/orders/{orderCode}";

    public static final String PRODUCTION_ORDER_ENDPOINT_PATH = "/{subscriberId}/production-orders/{poNumber}";

    public static final String PRODUCTION_ORDERS_PATH = "/{subscriberId}/production-orders";

    public static final String PRODUCTION_ORDERS_BY_STATUS_PATH = "/{subscriberId}/production-orders?status={status}";

    public static final String PRODUCTION_ORDER_CANCEL_ENDPOINT_PATH = "/{subscriberId}/production-orders/{poNumber}/cancel";

    public static final String PRODUCTION_ORDER_PROCEED_ENDPOINT_PATH = "/{subscriberId}/production-orders/{poNumber}/proceed";

    public static final String PLACE_PRODUCTION_ORDER_ENDPOINT_PATH = "/{subscriberId}/orders/{orderCode}/place-production-order";

    public static final String BILLING_CONFIGS_ENDPOINT_PATH = "/{subscriberId}/billing-configs";

    public static final String BILLINGS_ENDPOINT_PATH = "/{subscriberId}/billings";

    public static final String BILLING_ENDPOINT_PATH = "/{subscriberId}/billings/{billingUuid}";

    public static final String BILLING_INSTALLMENTS_ENDPOINT_PATH = "/{subscriberId}/billings/{billingUuid}/installments";

    public static final String BILLING_INSTALLMENT_ENDPOINT_PATH = "/{subscriberId}/billings/{billingUuid}/installments/{installmentUuid}";

    public static final String BILLING_INSTALLMENTS_BY_STATUS_PATH = "/{subscriberId}/installments";
}