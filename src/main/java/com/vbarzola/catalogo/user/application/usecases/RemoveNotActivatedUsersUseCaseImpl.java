package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.ports.in.RemoveNotActivatedUsersUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveNotActivatedUsersUseCaseImpl implements RemoveNotActivatedUsersUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveNotActivatedUsersUseCaseImpl.class);

    private final UserRepository userRepository;

    @Override
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                LOG.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
            });
    }
}
