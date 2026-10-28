package com.vbarzola.catalogo.user.domain.ports.in;

import com.vbarzola.catalogo.user.domain.model.User;
import java.util.Optional;

public interface RequestPasswordResetUseCase {
    Optional<User> requestPasswordReset(String email);
}
