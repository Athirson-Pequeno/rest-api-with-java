package com.tizo.br.model.vo.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class UserAccountRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public UserAccountRecordVO() {
    }

    private String email;
    private String username;
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccountRecordVO that)) return false;
        return Objects.equals(email, that.email) && Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, password);
    }
}
