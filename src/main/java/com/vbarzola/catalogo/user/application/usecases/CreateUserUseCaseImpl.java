package com.vbarzola.catalogo.user.application.usecases;

import com.vbarzola.catalogo.config.Constants;
import com.vbarzola.catalogo.user.domain.model.User;
import com.vbarzola.catalogo.user.domain.ports.in.CreateUserUseCase;
import com.vbarzola.catalogo.user.domain.ports.out.AuthorityRepository;
import com.vbarzola.catalogo.user.domain.ports.out.UserRepository;
import java.time.Instant;
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
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(CreateUserUseCaseImpl.class);

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        user.setLogin(user.getLogin().toLowerCase());
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().toLowerCase());
        }
        if (user.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        user.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (user.getAuthorities() != null) {
            Set<String> validAuthorities = new HashSet<>();
            for (String authorityName : user.getAuthorities()) {
                if (authorityRepository.findById(authorityName).isPresent()) {
                    validAuthorities.add(authorityName);
                }
            }
            user.setAuthorities(validAuthorities);
        }

        User savedUser = userRepository.save(user);
        LOG.debug("Created Information for User: {}", savedUser);
        return savedUser;
    }
}
