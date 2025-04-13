package identityservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name="username", unique = true)
    String username;

    @Column(name="password")
    String password;

    @Column(name="create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date createdt;

    @Column(name="update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedt;

    @PrePersist
    public void setCreatedDate() {
        this.createdt = new Date();
        this.updatedt = new Date();
    }

    @PreUpdate
    public void setUpdatedDate() {
        this.updatedt = new Date();
    }

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    boolean isDeleted;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<Role> roles;

}
