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
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author Rafael Chiarinelli
 *
 */
@Entity
@Table(name = "t_customer")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(
        value = {
            @NamedQuery(name = "getCustomersWithBillings"
                    , query = "select distinct ord.customer from Order ord inner join ord.billings as billing where ord.subscriber.id=:subscriberId")
        }        
)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Customer implements Serializable, Domain {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -6816166660674280801L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Integer id;

    @Column(name = "cust_name", length = 30, nullable = false)
    private String name;

    @Column(name = "cust_details", length = 10000, nullable = true)
    private String details;

    @Column(name = "cust_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CustomerStatus status;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_customer", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Set<CustomerAddress> addresses;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_customer", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Set<CustomerContact> contacts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_subscriber", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Subscriber subscriber;

    /**
     *
     * @param id
     * @param name
     */
    public Customer(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }


    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the details
     */
    public String getDetails() {
        return details;
    }

    /**
     * @param details the details to set
     */
    public void setDetails(String details) {
        this.details = details;
    }


    /**
     * @return the status
     */
    public CustomerStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    /**
     * @return the addresses
     */
    public Set<CustomerAddress> getAddresses() {
        return addresses;
    }

    /**
     * @param addresses the addresses to set
     */
    public void setAddresses(Set<CustomerAddress> addresses) {
        this.addresses = addresses;
    }

    /**
     * @return the contacts
     */
    public Set<CustomerContact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(Set<CustomerContact> contacts) {
        this.contacts = contacts;
    }

    /**
     * @return the subscriber
     */
    public Subscriber getSubscriber() {
        return subscriber;
    }

    /**
     * @param subscriber the subscriber to set
     */
    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }


    public void addCustomer(Customer customer) {

    }

}
