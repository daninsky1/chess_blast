package com.chessblast.model;


import javax.persistence.*;

@Entity(name = "User")
@Table(name = "users",
       uniqueConstraints = {@UniqueConstraint(name = "UK_student_username", columnNames = "username"),
                            @UniqueConstraint(name = "UK_student_email", columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username",
            nullable = false,
            length = 50)
    private String username;
    @Column(name = "email",
            nullable = false,
            length = 50)
    private String email;
    @Column(name = "password",
            nullable = false,
            length = 64)
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User() { }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("Customer[id='%d', firstName='%s', lastName='%s'",
                id, username, email);
    }
}
