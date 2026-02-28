package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.time.LocalTime;

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

/**
 * 
 * week start day = sunday = 1
 * week end day = saturday = 7
 * shift duration in minutes = 480
 */
@Entity
@Table(name = "t_work_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSchedule implements Serializable {

    public enum WorkScheduleStatus {
        ACTIVE, INACTIVE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_schedule_seq")
    @SequenceGenerator(name = "work_schedule_seq", sequenceName = "t_work_schedule_seq", allocationSize = 1)
    @Column(name = "id_work_schedule")
    private Long id;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private WorkScheduleStatus status;

    @Column(name = "week_start_day", nullable = false)
    private Integer weekStartDay;

    @Column(name = "week_end_day", nullable = false)
    private Integer weekEndDay;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "shift_duration_in_minutes", nullable = false)
    private Short shiftDurationInMinutes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subscriber", nullable = false)
    private Subscriber subscriber;
}
