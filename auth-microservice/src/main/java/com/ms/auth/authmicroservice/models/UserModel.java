package com.ms.auth.authmicroservice.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Entity
@Getter
@RequiredArgsConstructor
@Setter
@Table(name = "users_tb")
@ToString
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true, nullable = false)
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String verificationCode;

    @Column(nullable = false)
    private Boolean verified;

    public boolean isVerified() {
        return verified;
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserModel userModel = (UserModel) o;
        return getUuid() != null && Objects.equals(getUuid(), userModel.getUuid());
    }

    @Override public int hashCode() {
        return getClass().hashCode();
    }

}
