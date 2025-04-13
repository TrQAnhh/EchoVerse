package com.echoverse.profile.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String firstName;
    String middleName;
    String lastName;
    String chanelName;
    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    Set<UserProfile> followers;

    @ManyToMany(mappedBy = "followers")
    Set<UserProfile> following;
    String email;
    LocalDate dob;
    String address;
    String phoneNumber;
    String bio;
    String coverImage;
    String avatar;

    @Column(name="create_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date createdt;

    @Column(name="update_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedt;

    @Column
    long userId;

    @PrePersist
    public void setCreatedDate() {
        this.createdt = new Date();
        this.updatedt = new Date();
    }

    @PreUpdate
    public void setUpdatedDate() {
        this.updatedt = new Date();
    }




}
