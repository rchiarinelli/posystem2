package br.com.brainyit.posystem2.po.api;

import br.com.brainyit.posystem2.restbean.RestBean;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductionOrderRestBean extends RestBean {

    private String poNumber;
    
    private String description;
    
    private String status;
    
    private double estimatedTime;
    
    private String openDate;
    
    private String estimatedDueDate;
    
    private int quantity;

    private String actualDeliveryDate;

    private String customerName;
    
}
