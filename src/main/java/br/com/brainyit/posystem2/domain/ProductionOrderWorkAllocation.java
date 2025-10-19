package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOrderWorkAllocation implements Serializable {

    public enum ProductionOrderWorkAllocationStatus {
        PENDING, APPROVED, EXECUTING, COMPLETED, CANCELLED;

        // PENDING = The work allocation is pending to be reviewed
        // APPROVED = The work allocation is reviewed and approved
        // EXECUTING = The work allocation is executing
        // COMPLETED = The work allocation is completed
        // CANCELLED = The work allocation is cancelled
        // PAUSED = The work allocation is paused
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_order_work_allocation_seq")
    @SequenceGenerator(name = "production_order_work_allocation_seq", sequenceName = "t_production_order_work_allocation_seq", allocationSize = 1)
    @Column(name = "id_production_order_work_allocation")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_production_order", nullable = false)
    @Fetch(FetchMode.JOIN)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_work_resource", nullable = false)
    @Fetch(FetchMode.JOIN)
    private WorkResource workResource;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "hours_per_day", nullable = false)
    private Double hoursPerDay;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProductionOrderWorkAllocationStatus status = ProductionOrderWorkAllocationStatus.PENDING;
}
