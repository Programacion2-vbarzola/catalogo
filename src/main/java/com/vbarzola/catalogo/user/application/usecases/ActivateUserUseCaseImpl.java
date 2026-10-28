package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.ActivateUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivateUserUseCaseImpl implements ActivateUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(ActivateUserUseCaseImpl.class);

    private final UserRepository userRepository;

    @Override
    public Optional<User> activateUser(String key) {
        LOG.debug("Activating user for activation key {}", key);
        return userRepository
            .findOneByActivationKey(key)
            .map(user -> {
                user.setActivated(true);
                user.setActivationKey(null);
                User saved = userRepository.save(user);
                LOG.debug("Activated user: {}", saved);
                return saved;
            });
    }
}
