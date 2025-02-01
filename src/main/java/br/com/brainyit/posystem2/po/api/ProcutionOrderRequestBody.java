package br.com.brainyit.posystem2.po.api;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProcutionOrderRequestBody {

    @Min(value = 1, message = "Quantity must be greater than zero")
    private int qtd;

    @Min(value = 1, message = "Estimated time must be greater than zero")
    private double estimatedTime;
    
    private String description;
}
