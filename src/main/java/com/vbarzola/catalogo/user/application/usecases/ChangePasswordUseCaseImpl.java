package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.security.SecurityUtils;
import com.vbarzola.catalogo.user.application.exception.InvalidPasswordException;
import com.vbarzola.catalogo.user.domain.ports.in.ChangePasswordUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ChangePasswordUseCaseImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                LOG.debug("Changed password for User: {}", user);
            });
    }
}
