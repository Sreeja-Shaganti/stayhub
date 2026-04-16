package com.neo.stayhub.domain;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false, length = 150,  unique = true)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean accountLocked;

    @Column(nullable = false)
    private Boolean emailVerified;

    @Column(nullable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @Column(nullable = false)
    private Long version;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userUserRoles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Booking> userBookings = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Notification> userNotifications = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Review> userReviews = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
