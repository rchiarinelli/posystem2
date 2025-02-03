package br.com.brainyit.posystem2.po.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductionOrderRequestBody {

    private String description = "";

    @Min(message = "Quantity must be greater than zero", value = 1)
    private int qtd;
    
    @NotNull(message = "Estimated due date is required")
    private String estimatedDueDate;
    
    @NotNull(message = "Actual delivery date is required")
    private String actualDeliveryDate;
    
    @NotNull(message = "Status is required")
    private String status;
    
    @Min(message = "Estimated time must be greater than zero", value = 1)
    private double estimatedTime;

}
