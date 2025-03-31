package com.example.usermanagement.model;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.NonNull;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Email
    // must belong to @exaple.gov.br domain
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@example\\.gov\\.br$", message = "Email must belong to the example.gov.br domain")
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    // Manager
    // Users' can't be null,
    // Managers's can be null,
    // and Admins' must be null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "id", nullable = true)
    private User manager;

    // Emumerations
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;

    // Timestamps
    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime updatedAt;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deactivatedAt;

    // Public methods
    public boolean isManager() {
        return this.type == UserType.MANAGER;
    }

    public boolean isActive() {
        return this.deactivatedAt == null;
    }

}
