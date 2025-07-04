
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.brainyit.posystem2.order.repository.OrderRepository;
import br.com.brainyit.posystem2.po.ProductionOrder;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rafael Chiarinelli
 */
@Entity
@Table(name = "t_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;

    @ManyToOne(targetEntity = Customer.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer")
    @Fetch(FetchMode.JOIN)
    private Customer customer;

    @Column(name = "open_date", nullable = false)
    private Date openDate;

    @Column(name = "close_date", nullable = true)
    private Date closeDate;

    @Column(name = "order_description", nullable = false, length = 255)
    private String description;

    @Column(name = "order_price", nullable = true)
    private Double price;

    @Column(name = "qtd_item", nullable = false)
    private Double quantity;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @Column(name = "order_code", nullable = false, length = 100, unique = true)
    private String orderCode;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ProductionOrder.class)
    @JoinColumn(name = "id_order")
    @Fetch(FetchMode.JOIN)
    private List<ProductionOrder> productionOrders;

    @Column(name = "deliver_date", nullable = true)
    private Date deliverDate;

    @Column(name = "planned_deliver_date", nullable = true)
    private Date plannedDeliverDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order")
    @Fetch(FetchMode.JOIN)
    private List<Billing> billings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subscriber")
    @Fetch(FetchMode.JOIN)
    private Subscriber subscriber;

    @Transient
    private OrderRepository orderRepository;

}
