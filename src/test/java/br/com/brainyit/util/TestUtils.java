package br.com.brainyit.util;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import br.com.brainyit.posystem2.domain.Company;
import br.com.brainyit.posystem2.domain.Customer;
import br.com.brainyit.posystem2.domain.Individual;
import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;

public class TestUtils {

    public static final String CUSTOMER_ID = "5";

    public static final String SUBSCRIBER = "1";

    private static final Gson GSON = new Gson();

    private TestUtils() {
        throw new UnsupportedOperationException("This is a utility class and should not be instantiated");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> convertResponseToMap(final String response) {
        return GSON.fromJson(response, Map.class);
    }

    public static List<Map<String, Object>> convertResponseToList(final String response) {
        return GSON.fromJson(response, List.class);
    }

    public static Customer createIndividualCustomer(final String name, final String document) {
        return Individual.builder()
                .name(name)
                .cpf(document)
                .build();
    }

    public static Customer createCompanyCustomer(final String name, final String document) {
        return Company.builder()
                .name(name)
                .cnpjCgc(document)
                .build();
    }

    public static Order createOrder(final Customer customer, final String description, final Double quantity,
            final OrderStatus status, final Date openDate, final String orderCode) {
        return Order.builder()
                .customer(customer)
                .description(description)
                .quantity(quantity)
                .status(status)
                .openDate(openDate)
                .orderCode(orderCode)
                .build();
    }
}
