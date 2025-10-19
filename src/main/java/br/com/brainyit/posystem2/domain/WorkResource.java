package br.com.brainyit.posystem2.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "t_work_resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkResource implements Serializable {

    public enum WorkResourceStatus {
        ACTIVE, INACTIVE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_resource_seq")
    @SequenceGenerator(name = "work_resource_seq", sequenceName = "t_work_resource_seq", allocationSize = 1)
    @Column(name = "id_work_resource")
    private Long id;

    @Column(name = "setup_time_in_minutes", nullable = false)
    private Short setupTimeInMinutes;

    @Column(name = "cost_per_hour", nullable = false)
    private Double costPerHour;

    @Column(name = "type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ServiceType type;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private WorkResourceStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_work_schedule", nullable = false)
    private WorkSchedule workSchedule;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = true, length = 255)
    private String description;
}
