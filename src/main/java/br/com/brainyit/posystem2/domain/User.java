/**
 * 
 */
package br.com.brainyit.posystem2.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author leafar
 *
 */

 @Data
 @Builder
 @AllArgsConstructor
 @NoArgsConstructor
 @Entity(name = "t_user")
public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1822333369324134675L;

	// @Id
	// @Column(name="user_login",nullable=false)
	// private String login;
	
	// @Column(name="user_password",nullable=false)
	// private String password;
	
	// @Column(name="user_email",nullable=false)
	// private String email;
	
	// @ManyToOne(fetch=FetchType.EAGER,targetEntity=Subscriber.class)
	// @JoinColumn(name="id_subscriber",nullable=false)
	// @Fetch(FetchMode.JOIN)
	// private Subscriber subscriber;
	
	// @OneToMany(cascade={CascadeType.ALL},targetEntity=Permission.class,fetch=FetchType.LAZY)
	// @JoinColumn(name="user_login")
	// @Fetch(FetchMode.JOIN)
	// private Set<Permission> permissions;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(nullable = false, length = 60, name = "uuid")
    private String uuid = UUID.randomUUID().toString();

    @Column(nullable = true, length = 50, name = "first_name")
    private String firstName;

    @Column(nullable = true, length = 50, name = "last_name")
    private String lastName;

    @Column(nullable = false, length = 50, name = "email")
    private String email;

    @Column(nullable = false, length = 50, name = "user_name")
    private String userName;

    @Column(nullable = false, length = 50, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 50, name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    private Subscriber subscriber;

}
