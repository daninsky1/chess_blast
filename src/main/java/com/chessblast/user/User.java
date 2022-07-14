package com.chessblast.user;


import com.chessblast.Game;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity(name = "User")
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(name = "UK_user_username", columnNames = "username"),
        @UniqueConstraint(name = "UK_user_email", columnNames = "email")
    }
)
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(
        name = "username",
        nullable = false,
        length = 50
    )
    private String username;

    @Column(
        name = "email",
        nullable = false,
        length = 50
    )
    private String email;

    @Column(
        name = "password",
        nullable = false,
        length = 64
    )
    private String password;

    @ManyToMany(mappedBy = "participants")
    Set<Game> participate;

    private final UserRole userRole = UserRole.PLAYER;

    public User(long id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);    // NOTE: returns an immutable list
    }

    public Long getId() {
        return id;
    }

    @Override
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("User[id='%d', firstName='%s', lastName='%s'",
                id, username, email);
    }
}
