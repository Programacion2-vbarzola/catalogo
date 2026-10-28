package com.vbarzola.catalogo.user.domain.ports.in;

import com.vbarzola.catalogo.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllUsersUseCase {
    Page<User> getAllUsers(Pageable pageable);
}
