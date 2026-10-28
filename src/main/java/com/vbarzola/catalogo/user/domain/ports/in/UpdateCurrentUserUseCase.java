package com.vbarzola.catalogo.user.domain.ports.in;

public interface UpdateCurrentUserUseCase {
    void updateCurrentUser(String firstName, String lastName, String email, String langKey, String imageUrl);
}
