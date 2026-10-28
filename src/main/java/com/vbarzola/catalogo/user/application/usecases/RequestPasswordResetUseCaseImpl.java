package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.RequestPasswordResetUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tech.jhipster.security.RandomUtil;

@Component
@RequiredArgsConstructor
public class RequestPasswordResetUseCaseImpl implements RequestPasswordResetUseCase {

    private final UserRepository userRepository;

    @Override
    public Optional<User> requestPasswordReset(String email) {
        return userRepository
            .findOneByEmailIgnoreCase(email)
            .filter(User::isActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return userRepository.save(user);
            });
    }
}
