package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.ports.in.DeleteUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(DeleteUserUseCaseImpl.class);

    private final UserRepository userRepository;

    @Override
    public void deleteUser(String login) {
        userRepository
            .findOneByLogin(login)
            .ifPresent(user -> {
                userRepository.delete(user);
                LOG.debug("Deleted User: {}", user);
            });
    }
}
