package com.vbarzola.catalogo.user.domain.ports.in;

import com.vbarzola.catalogo.user.domain.model.User;

public interface RegisterUserUseCase {
    User registerUser(User user, String rawPassword);
}
