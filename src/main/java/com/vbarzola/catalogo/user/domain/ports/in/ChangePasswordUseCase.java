package com.vbarzola.catalogo.user.domain.ports.in;

public interface ChangePasswordUseCase {
    void changePassword(String currentClearTextPassword, String newPassword);
}
