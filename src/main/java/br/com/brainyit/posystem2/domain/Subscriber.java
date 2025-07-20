/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leafar
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_subscriber")
public class Subscriber implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -6709193260259588229L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subscriber_seq")
    @SequenceGenerator(name = "subscriber_seq", sequenceName = "t_subscriber_seq", allocationSize = 1)
    private Long id;

    @Builder.Default
    @Column(nullable = false, length = 60)
    private String uuid = UUID.randomUUID().toString();

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 50)
    private String email;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<User> users;

}
