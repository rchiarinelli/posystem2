/**
 *
 */
package br.com.brainyit.posystem2.domain;

import br.com.brainyit.posystem2.exception.BusinessException;
import br.com.brainyit.posystem2.exception.SystemException;
import br.com.brainyit.posystem2.repository.OrderRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author Rafael Chiarinelli
 */
@Component
@Entity
@Table(name = "t_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order implements Serializable {

    private static Logger logger = LogManager.getLogger(Order.class);

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
    private Integer quantity;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private OrderStatus status;

    @Column(name = "order_code", nullable = false, length = 20)
    private String orderCode;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ProductionOrder.class)
    @JoinColumn(name = "id_order")
    @Fetch(FetchMode.JOIN)
    private Set<ProductionOrder> productionOrders;

    @Column(name = "deliver_date", nullable = true)
    private Date deliverDate;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order")
    @Fetch(FetchMode.JOIN)
    private Set<Billing> billings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subscriber")
    @Fetch(FetchMode.JOIN)
    private Subscriber subscriber;

    @Transient
    private OrderRepository orderRepository;

    /**
     * @param id
     */
    public void close(Long id) {

    }

    /**
     * @param id
     */
    public void cancel(Long id) {


    }

    /**
     * @param id
     */
    public void bill(Long id) {

    }

    /**
     * @param order
     * @param po
     * @return
     */
    public Long addPO(Order order, ProductionOrder po) {
        throw new UnsupportedOperationException("Operation not implemented.");
    }

    /**
     * Realiza a execucao da venda, retornando o ID gerado.
     *
     * @param order
     * @return
     * @throws BusinessException
     */
    public Long perform(Order order) throws BusinessException, SystemException {
        logger.debug("Performing the order.");
        return orderRepository.saveAndFlush(order).getId();
    }
}
