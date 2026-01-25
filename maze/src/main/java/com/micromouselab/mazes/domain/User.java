package com.micromouselab.mazes.domain;

import jakarta.persistence.*;

import static com.micromouselab.mazes.domain.Role.USER;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 256)
    private String username;

    @Column(nullable = false)
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    private Role role;

    public User(){}

    public User(String username, String hashedPassword){
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = Role.USER;
    }

    public User(String username, String hashedPassword, Role role){
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }


    public String getHashedPassword() {
        return hashedPassword;
    }

    public Role getRole() {return role;}


    public Long getId() {
        return id;
    }

}
