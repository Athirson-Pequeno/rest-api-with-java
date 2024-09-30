package com.tizo.br.model.vo.security;

import java.io.Serial;
import java.io.Serializable;

public record UserAccountRecordVO(
        String email,
        String username,
        String password
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
