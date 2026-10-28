package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.security.AuthoritiesConstants;
import com.vbarzola.catalogo.user.application.exception.EmailAlreadyUsedException;
import com.vbarzola.catalogo.user.application.exception.UsernameAlreadyUsedException;
import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.RegisterUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tech.jhipster.security.RandomUtil;

@Component
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(RegisterUserUseCaseImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user, String rawPassword) {
        userRepository
            .findOneByLogin(user.getLogin().toLowerCase())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new UsernameAlreadyUsedException();
                }
            });
        userRepository
            .findOneByEmailIgnoreCase(user.getEmail())
            .ifPresent(existingUser -> {
                boolean removed = removeNonActivatedUser(existingUser);
                if (!removed) {
                    throw new EmailAlreadyUsedException();
                }
            });

        user.setLogin(user.getLogin().toLowerCase());
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().toLowerCase());
        }
        user.setActivated(false);
        user.setActivationKey(RandomUtil.generateActivationKey());
        Set<String> authorities = new HashSet<>();
        authorities.add(AuthoritiesConstants.USER);
        user.setAuthorities(authorities);

        User savedUser = userRepository.save(user);
        LOG.debug("Created Information for User: {}", savedUser);
        return savedUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }
}
