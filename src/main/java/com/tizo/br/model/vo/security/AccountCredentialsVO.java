package com.tizo.br.model.vo.security;

import java.io.Serial;
import java.io.Serializable;

public record AccountCredentialsVO(String email,
                                   String password) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}
