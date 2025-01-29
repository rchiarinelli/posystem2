package br.com.brainyit.posystem2.order.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRestBean {

    private String orderCode;

    private String customerName;

    private String orderDescription;

    private Double price;

    private Double quantity;

    private String deliverDate;

    private String status;

}
