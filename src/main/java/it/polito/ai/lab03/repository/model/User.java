package it.polito.ai.lab03.repository.model;

import it.polito.ai.lab03.repository.model.position.Position;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id;

    private String username;
    private String password;
    private boolean accountExpired;
    private List<Position> positions;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        if (this.positions == null)
            this.positions = positions;
        else
            this.positions.addAll(positions);
    }

    private List<GrantedAuthority> authorities;

    public String getRole() {
        return role;
    }

    private String role;

    public void setRole(String role) {
        this.role = role;
    }

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.accountExpired = false;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = "ROLE_USER";
        this.accountExpired = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

}
