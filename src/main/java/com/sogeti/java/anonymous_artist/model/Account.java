package com.sogeti.java.anonymous_artist.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @Email(message = "Please fill in a valid email address. Example: example@example.com")
    private String email;
    private String password;

    @OneToOne
    @JoinColumn(name = "membership_id")
    private User user;

    @OneToMany(targetEntity = Authority.class,
            mappedBy = "email",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }
}
