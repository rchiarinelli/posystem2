/**
 * 
 */
package br.com.brainyit.posystem2.po;

import java.io.Serializable;
import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.brainyit.posystem2.domain.Order;
import br.com.brainyit.posystem2.domain.OrderStatus;
import br.com.brainyit.posystem2.domain.POPriority;
import br.com.brainyit.posystem2.domain.POStatus;
import br.com.brainyit.posystem2.domain.Subscriber;
import br.com.brainyit.posystem2.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rchiari
 *
 */
@Entity
@Table(name = "t_production_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductionOrder implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "production_order_seq")
	@SequenceGenerator(name = "production_order_seq", sequenceName = "t_production_order_seq", allocationSize = 1)
	@Column(name = "id_po")
	private Long id;

	@ManyToOne(targetEntity = Order.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "id_order")
	@Fetch(FetchMode.JOIN)
	private Order order;

	@Column(name = "po_number", nullable = false, unique = true, length = 100)
	private String poNumber;

	@Column(name = "instructions", nullable = true, length = 255)
	private String instructions;

	@Builder.Default
	@Column(name = "qtd_pieces", nullable = true)
	private Integer qtd = 0;

	@Column(name = "estimated_completion_date", nullable = true)
	private LocalDate estimatedCompletionDate;

	@Column(name = "actual_completion_date", nullable = true)
	private LocalDate actualCompletionDate;

	@Column(name = "planned_start_date", nullable = false)
	private LocalDate plannedStartDate;

	@Column(name = "actual_start_date", nullable = true)
	private LocalDate actualStartDate;

	@Column(name = "po_status", nullable = false)
	private POStatus status;

	@Builder.Default
	@Column(name = "priority", nullable = false)
	private POPriority priority = POPriority.STANDARD;

	@Builder.Default
	@Column(name = "estimated_time", nullable = false)
	private Double estimatedTime = 0.0;

	@Column(name = "open_date", nullable = false)
	private LocalDate openDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_subscriber", nullable = false)
	@Fetch(FetchMode.JOIN)
	private Subscriber subscriber;

	public ProductionOrder moveToNextPhase() {
		if (status == POStatus.PLANNING) {
			status = POStatus.EXECUTING;
			actualStartDate = LocalDate.now();
		} else if (status == POStatus.EXECUTING) {
			if (order.getProductionOrders() != null && order.getProductionOrders().stream()
					.noneMatch(po -> po.getId() != this.id && po.getStatus() == POStatus.EXECUTING)) {
				order.setStatus(OrderStatus.DELIVERED);
			}
			status = POStatus.DONE;
			actualCompletionDate = LocalDate.now();
		} else if (status == POStatus.CANCELLED || status == POStatus.DONE) {
			throw new BusinessException(String.format("Cannot move production order from %s status", status));
		}
		return this;
	}

	public ProductionOrder cancel(final String instructions) {
		if (status == POStatus.PLANNING || status == POStatus.EXECUTING) {
			status = POStatus.CANCELLED;
		} else if (status == POStatus.DONE) {
			throw new BusinessException("Cannot cancel production order in DONE status");
		}
		if (StringUtils.isBlank(instructions)) {
			throw new BusinessException("Instructions cannot be empty");
		}
		this.instructions += "\n" + instructions;
		this.actualCompletionDate = LocalDate.now();
		this.status = POStatus.CANCELLED;
		return this;
	}
}
