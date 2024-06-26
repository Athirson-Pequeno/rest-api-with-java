package com.tizo.br.model.vo.security;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class AccountCredentialsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public AccountCredentialsVO() {
    }

    private String email;
    private String password;

    public AccountCredentialsVO(String email, String password) {
        this.email = email;
        this.password = password;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountCredentialsVO that)) return false;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
