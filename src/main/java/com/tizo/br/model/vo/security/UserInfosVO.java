package com.tizo.br.model.vo.security;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserInfosVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserInfosVO() {
    }

    private Long id;
    private String email;
    private String username;
    private Boolean enabled;
    private List<String> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfosVO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(username, that.username) && Objects.equals(enabled, that.enabled) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, enabled, roles);
    }
}
