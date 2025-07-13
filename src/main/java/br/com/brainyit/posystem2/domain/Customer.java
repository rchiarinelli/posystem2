/**
 *
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Rafael Chiarinelli
 *
 */
@Entity
@Table(name = "t_customer")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {
        @NamedQuery(name = "getCustomersWithBillings", query = "select distinct ord.customer from Order ord inner join ord.billings as billing where ord.subscriber.id=:subscriberId")
})
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Customer implements Serializable, Domain {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -6816166660674280801L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "t_customer_seq", allocationSize = 1)
    @Column(name = "id_customer")
    private Integer id;

    @Column(name = "cust_name", length = 30, nullable = false)
    private String name;

    @Column(name = "cust_details", length = 10000, nullable = true)
    private String details;

    @Column(name = "cust_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CustomerStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "id_customer", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Set<CustomerAddress> addresses;

    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @JoinColumn(name = "id_customer", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Set<CustomerContact> contacts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subscriber", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Subscriber subscriber;

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
