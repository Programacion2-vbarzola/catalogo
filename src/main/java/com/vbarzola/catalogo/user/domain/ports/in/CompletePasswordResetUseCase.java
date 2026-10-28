package com.vbarzola.catalogo.user.domain.ports.in;

import com.vbarzola.catalogo.user.domain.model.User;
import java.util.Optional;

public interface CompletePasswordResetUseCase {
    Optional<User> completePasswordReset(String newPassword, String key);
}
