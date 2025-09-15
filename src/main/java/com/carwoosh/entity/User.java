package com.carwoosh.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic info
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 15)
    private String mobileNumber;

    @Column(nullable = false)
    private String password;

    private String profileImageUrl;

    // Tracking
    private LocalDateTime lastLogin;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Role-based access (USER, ADMIN, MECHANIC, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistory> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vehicle> vehicles;

    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.role = Role.USER;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Role {
        USER,
        ADMIN,
        MECHANIC
    }
}
