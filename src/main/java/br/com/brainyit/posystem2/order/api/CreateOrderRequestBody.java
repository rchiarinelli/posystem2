package br.com.brainyit.posystem2.order.api;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class CreateOrderRequestBody {

    @NotNull
    private String customerName;

    @NotNull
    private String customerDocument;

    @NotNull
    private String orderDescription;

    @NotNull
    private Double quantity;
}
