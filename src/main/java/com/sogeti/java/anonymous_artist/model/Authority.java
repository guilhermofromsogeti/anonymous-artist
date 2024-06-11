package com.sogeti.java.anonymous_artist.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
@IdClass(Authority.class)
@Table(name = "authorities")
public class Authority implements Serializable {

    @Id
    @Column(nullable = false)
    private String email;

    @Id
    @Column(nullable = false)
    private String authority;

    public Authority(String email, String authority) {
        this.email = email;
        this.authority = authority;
    }

    public void setUsername(String email) {
        this.email = email;
    }

}
