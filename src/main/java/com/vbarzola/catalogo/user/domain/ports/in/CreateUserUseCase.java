package com.vbarzola.catalogo.user.domain.ports.in;

import com.vbarzola.catalogo.user.domain.model.User;

public interface CreateUserUseCase {
    User createUser(User user);
}
