package br.com.brainyit.posystem2.order.service;

import java.util.List;

public record FilterOrderParams(
        String name,
        String orderCode,
        List<String> status) {

    public static FilterOrderParams of() {
        return new FilterOrderParams(null, null, null);
    }

    public static FilterOrderParams of(final String name, final String orderCode, final List<String> status) {
        return new FilterOrderParams(name, orderCode, status);
    }
}
